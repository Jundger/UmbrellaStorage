package com.jundger.work.controller;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.CreateRandomCharacter;
import com.jundger.common.util.ToMap;
import com.jundger.work.constant.CellStatusEnum;
import com.jundger.work.constant.Consts;
import com.jundger.work.constant.OrderStatusEnum;
import com.jundger.work.pojo.Cell;
import com.jundger.work.pojo.Storage;
import com.jundger.work.pojo.User;
import com.jundger.work.quartz.QuartzUtil;
import com.jundger.work.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.criteria.Order;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
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
@RequestMapping(value = "mini")
public class MiniController {

	@Resource
	private UserService userService;

	QuartzUtil quartzUtil = new QuartzUtil();

	private static Logger logger = Logger.getLogger(MiniController.class);

	/**
	 * 获取附近设备列表
	 * @param openid 微信身份id
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @return 设备集合
	 */
	@ResponseBody
	@RequestMapping(value = "getTerminalList", produces = "application/json; charset=utf-8")
	public String getTerminalList(HttpServletRequest request, HttpServletResponse response,
								  @RequestParam(value = "openid") String openid,
								  @RequestParam(value = "longitude") String longitude,
								  @RequestParam(value = "latitude") String latitude) {
		return null;
	}


	/**
	 * 雨伞存储服务
	 * @param openid 微信身份id
	 * @param terminal_id 储伞柜终端id
	 * @param duration 存储时长
	 * @param type 类型：存/还
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "wxstor", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String wxStorageUmbrella(HttpServletRequest request, HttpServletResponse response,
								  @RequestParam(value = "openid") String openid,
								  @RequestParam(value = "terminal_id") Integer terminal_id,
								  @RequestParam(value = "duration") Integer duration,
								  @RequestParam(value = "type") String type) {

		User user;
		Storage storage;
		logger.info("coming................");

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		// 根据openid查询用户是否已有正在进行的订单
		user = userService.getUserByOpenId(openid);
		storage = userService.getRunningOrderById(user.getId());
		if (storage == null) {
			String order_no = CreateRandomCharacter.getOrderno();

			Cell cell = userService.getAvailableCell(terminal_id);
			if (cell == null) {
				logger.info(new Date() + "：该终端已没有可用空间！");
				returnMsg.put("return_code", "2");
				returnMsg.put("msg", "NONE_AVAILABLE");
				return ToMap.MapToJson(returnMsg);
			}
			// 使储伞格处于预订状态
			cell.setBookFlag(Consts.CELL_BOOK_FLAG_ON);
			logger.info(JSON.toJSONStringWithDateFormat(cell, "yyyy-MM-dd HH:mm:ss"));
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
			String keyt = CreateRandomCharacter.getRandomString(Consts.KEY_LENGTH);
			storage.setKeyt(keyt);
			userService.addStorageOrder(storage); // 新增订单

			returnMsg.put("return_code", "1");
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
			returnMsg.put("return_code", "0");
			returnMsg.put("msg", "该用户尚有未完成的订单");
		}

		return ToMap.MapToJson(returnMsg);
	}

	@ResponseBody
	@RequestMapping(value = "terstor", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String terStorageUmbrella(HttpServletRequest request, HttpServletResponse response,
								 @RequestParam(value = "terminal_id") Integer terminal_id,
								 @RequestParam(value = "qrcode") String qrcode) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		// 解析二维码数据
		String[] part = new String[3];
		part = qrcode.split("\\.");
		String order_no = part[0];
		String keyt = part[1];
		String type = part[2].trim();

		// 将二维码中的数据与数据库中的相应信息进行比对，判断二维码的正确性
		Storage storage = userService.getOrderForCheck(order_no, keyt, terminal_id);
		if (!OrderStatusEnum.WAITING.toString().equals(storage.getOrderStatus())) {
			returnMsg.put("return_code", "0");
			returnMsg.put("msg", storage.getOrderStatus());
			return ToMap.MapToJson(returnMsg);
		}

		// 订单开始，改变订单和储物格状态，关闭对应超时的定时任务调度
		userService.updateBeginStatus(storage.getOrderNo());

		// 计时开始，到期后终止订单
		quartzUtil.createExpireQuartz(quartzUtil.getNewExpireJobName(), quartzUtil.getNewExpireTriggerName(), order_no, storage.getPurchaseDuration());

		User user = userService.getUserById(storage.getUserId());
		// 生成返回数据包，包括用户名、储物格id和存取类型等
		returnMsg.put("return_code", "1");
		returnMsg.put("msg", "TRANSACTION_SUCCESS");
		returnMsg.put("nickname", user.getNickname());
		returnMsg.put("cell_id", storage.getCellId());
		returnMsg.put("type", type);

		return ToMap.MapToJson(returnMsg);
	}

//	public static void main(String[] args) {
//		QuartzUtil quartzUtil = new QuartzUtil();
//
//
//		quartzUtil.createExpireQuartz(quartzUtil.getNewExpireJobName(), quartzUtil.getNewExpireTriggerName(), "aaaaa", 1000L);
//
//		quartzUtil.createExpireQuartz(quartzUtil.getNewExpireJobName(), quartzUtil.getNewExpireTriggerName(), "bbbbb", 5 * 1000L);
//
//		quartzUtil.createExpireQuartz(quartzUtil.getNewExpireJobName(), quartzUtil.getNewExpireTriggerName(), "ccccc", 8 * 1000L);
//
//		quartzUtil.createOvertimeQuartz(quartzUtil.getNewOvertimeJobName(), quartzUtil.getNewOvertimeTriggerName(), "00001", 2 * 1000L);
//
//		quartzUtil.createOvertimeQuartz(quartzUtil.getNewOvertimeJobName(), quartzUtil.getNewOvertimeTriggerName(), "00002", 15 * 1000L);
//
//		quartzUtil.createOvertimeQuartz(quartzUtil.getNewOvertimeJobName(), quartzUtil.getNewOvertimeTriggerName(), "00003", 18 * 1000L);
//	}
}
