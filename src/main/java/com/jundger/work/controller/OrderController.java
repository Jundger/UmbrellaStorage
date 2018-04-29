package com.jundger.work.controller;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.CreateRandomCharacter;
import com.jundger.work.constant.Consts;
import com.jundger.work.constant.OrderStatusEnum;
import com.jundger.work.pojo.Cell;
import com.jundger.work.pojo.Storage;
import com.jundger.work.pojo.User;
import com.jundger.work.quartz.QuartzUtil;
import com.jundger.work.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/17 14:11
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Controller
@RequestMapping(value = "order")
public class OrderController {

	@Resource
	private UserService userService;

	private QuartzUtil quartzUtil = new QuartzUtil();

	private static Logger logger = Logger.getLogger(OrderController.class);

	/**
	 * 创建订单
	 * @param openid 微信身份id
	 * @param terminal_id 储伞柜终端id
	 * @param duration 存储时长
	 * @return 返回预创建的订单信息
	 */
	@ResponseBody
	@RequestMapping(value = "create", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> createOrder(HttpServletRequest request, @RequestParam(value = "openid") String openid,
										   @RequestParam(value = "terminal_id") Integer terminal_id,
										   @RequestParam(value = "duration") Integer duration,
										   @RequestParam(value = "types", required = false, defaultValue = Consts.TYPE_OF_STORAGE) String type) {

		User user;
		Storage storage;
		logger.info("====================创建订单接口调用=====================");

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		// 根据openid查询用户是否已有正在进行的订单
		user = userService.getUserByOpenId(openid);
		if (user == null) {
			user = new User();
			user.setOpenId(openid);
			user.setLoginIp(request.getRemoteAddr());
			userService.addUserSelective(user);
		}
		storage = userService.getRunningOrderById(user.getId());
		if (storage == null) {
			String order_no = CreateRandomCharacter.getOrderno();

			if (!Consts.TYPE_OF_STORAGE.equals(type) && !Consts.TYPE_OF_BORROW.equals(type)) {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "PARAM_TYPE_ERROR");
				return returnMsg;
			}
			Cell cell = userService.getAvailableCell(terminal_id, type);
			if (cell == null) {
				logger.info(new Date() + "：该终端已没有可用空间！");
				returnMsg.put("code", "0");
				returnMsg.put("msg", "NONE_AVAILABLE_SPACE");
				return returnMsg;
			}
			// 使储伞格处于预订状态
			cell.setBookFlag(Consts.CELL_BOOK_FLAG_ON);
//			logger.info(JSON.toJSONStringWithDateFormat(cell, "yyyy-MM-dd HH:mm:ss"));
			cell.setUseCount(cell.getUseCount() + 1);
			userService.updateCellStatus(cell);

			// 分配空闲储物格生成订单详细信息并保存
			storage = new Storage();
			storage.setOrderNo(order_no);
			storage.setPurchaseDuration(duration);
			storage.setTerminalId(terminal_id);
			storage.setCellId(cell.getId());
			storage.setOrderStatus(OrderStatusEnum.WAITING.toString());
			storage.setUserId(user.getId());
			storage.setDetail(type);
			String keyt = CreateRandomCharacter.getRandomString(Consts.KEY_LENGTH);
			storage.setKeyt(keyt);
			userService.addStorageOrder(storage); // 新增订单

			returnMsg.put("code", "1");
			returnMsg.put("user_id", user.getId());
			returnMsg.put("username", user.getNickname());
			returnMsg.put("order_no", order_no);
			returnMsg.put("duration", duration);
			returnMsg.put("terminal_id", terminal_id);
			returnMsg.put("cell_id", cell.getId());
			returnMsg.put("key", keyt);
			returnMsg.put("type", type);
			returnMsg.put("msg", "CREATE_ORDER_SUCCESS");

			// 新增订单成功，设置一个超时时间，超过一定时长未进行服务则终止订单
			quartzUtil.createOvertimeQuartz(quartzUtil.getNewOvertimeJobName(), quartzUtil.getNewOvertimeTriggerName(), order_no, Consts.ORDER_OVERTIME_TIME);
		} else {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "EXIST_UNFINISHED_ORDER");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public Map<String, Object> cancelOrder(@RequestParam(value = "order_no") String order_no) {

		logger.info("====================订单取消接口调用=====================");

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		try {
			String result = userService.cancelOrder(order_no);
			if ("CANCEL_ORDER_SUCCESS".equals(result)) {
				returnMsg.put("code", "1");
			}
			returnMsg.put("code", "0");
			returnMsg.put("msg", result);

		} catch (Exception e) {
			e.printStackTrace();

			returnMsg.put("code", "0");
			returnMsg.put("msg", "UNKNOWN_ERROR");
		}

		return returnMsg;
	}

	/**
	 * 订单开始
	 * 用户在微信端预创建订单后到终端设备开始使用服务
	 * 由终端设备调用此接口时正式开始服务计时
	 * @param terminal_id 终端机器号
	 * @param qrcode 终端扫描二维码所得到的数据
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value = "start", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> startOrder(@RequestParam(value = "terminal_id") Integer terminal_id,
										  @RequestParam(value = "qrcode") String qrcode) {
		logger.info("====================订单开始接口调用=======================");

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		logger.info("qrcode-->" + qrcode);

		try {
			/**
			 * 解析二维码数据
			 * 格式：订单号.密钥.操作类型
			 * 例：20180418020056725.IhSgjzxLQcRh58Pr3hcmUeSylZTt8IY6.STORAGE
			 */
			String[] part = new String[3];
			part = qrcode.split("\\."); // 分隔符为. | 等字符时需要进行转义
			String order_no = part[0];
			String keyt = part[1];
			String type = part[2].trim();

			// 将二维码中的数据与数据库中的相应信息进行比对，判断二维码的正确性
			Storage storage = userService.getOrderForCheck(order_no, keyt, terminal_id);
			if (null == storage) {
				returnMsg.put("code", "0"); // PARAM_UNPAIRED
				return returnMsg;
			} else if (OrderStatusEnum.OVERTIME.toString().equals(storage.getOrderStatus())) {
				returnMsg.put("code", "2");
				return returnMsg;
			} else if (OrderStatusEnum.RUNNING.toString().equals(storage.getOrderStatus())) {
				returnMsg.put("code", "3");
				return returnMsg;
			} else if (OrderStatusEnum.EXPIRE.toString().equals(storage.getOrderStatus())) {
				returnMsg.put("code", "4");
				return returnMsg;
			} else if (OrderStatusEnum.FINISH.toString().equals(storage.getOrderStatus())) {
				returnMsg.put("code", "5");
				return returnMsg;
			}

			// 订单开始，改变订单和储物格状态，关闭对应超时的定时任务调度
			userService.updateBeginStatus(storage.getOrderNo());

			// 计时开始，到期后终止订单
			quartzUtil.createExpireQuartz(quartzUtil.getNewExpireJobName(), quartzUtil.getNewExpireTriggerName(), order_no, storage.getPurchaseDuration());

			User user = userService.getUserById(storage.getUserId());
			// 生成返回数据包，包括用户名、储物格id和存取类型等
			returnMsg.put("code", "1");
//			returnMsg.put("order_no", order_no);
			returnMsg.put("cell_id", storage.getCellId());
			returnMsg.put("type", type.equals("STORAGE") ? "0" : "1");
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "6");
		}
		return returnMsg;
	}

	/**
	 * 用户在规定时间内从终端设备正常完成订单
	 * @param qrcode 终端扫描二维码所得到的数据
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value = "finish", method = RequestMethod.POST)
	public Map<String, Object> finishOrder(@RequestParam(value = "terminal_id") Integer terminal_id,
										   @RequestParam(value = "qrcode") String qrcode) {

		logger.info("====================订单结束接口调用=====================");

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		logger.info("qrcode-->" + qrcode);

		try {
			/**
			 * 解析二维码数据
			 * 格式：订单号.密钥.操作类型
			 * 例：20180418020056725.IhSgjzxLQcRh58Pr3hcmUeSylZTt8IY6.STORAGE
			 */
			String[] part = new String[3];
			part = qrcode.split("\\."); // 分隔符为. | 等字符时需要进行转义
			String order_no = part[0];
			String keyt = part[1];
			String type = part[2].trim();


			// 将二维码中的数据与数据库中的相应信息进行比对，判断二维码的正确性
			Storage storage = userService.getOrderForCheck(order_no, keyt, terminal_id);
			if (null == storage) {
				returnMsg.put("code", "0"); // PARAM_UNPAIRED
				return returnMsg;
			}

			// 查找到对应订单将状态RUNNING改为FINISH,并将cell表中book_flag置0，status改为AVAILABLE
			String result = userService.finishOrder(storage);

			returnMsg.put("code", result);

		} catch (Exception e) {
			returnMsg.put("code", "4"); // UNKNOWN_ERROR
		}

		return returnMsg;
	}

	/**
	 * 查询订单
	 * @param openid 微信号
	 * @return 查询结果
	 */
	@ResponseBody
	@RequestMapping(value = "query", method = RequestMethod.POST)
	public Map<String, Object> queryOrder(@RequestParam(value = "openid") String openid) {
		logger.info("====================查询订单接口调用=====================");

		logger.info("Query order by : " + openid);

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		List<Storage> storages = userService.queryOrder(openid);
		if (null != storages && !storages.isEmpty()) {
			returnMsg.put("code", "1");
			returnMsg.put("msg", "QUERY_SUCCESS");
			returnMsg.put("data", storages);
		} else {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
		}

		return returnMsg;
	}
}
