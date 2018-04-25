package com.jundger.work.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.jundger.work.constant.OrderStatusEnum;
import com.jundger.work.dao.CellMapper;
import com.jundger.work.dao.StorageMapper;
import com.jundger.work.dao.TerminalMapper;
import com.jundger.work.dao.UserMapper;
import com.jundger.work.pojo.Cell;
import com.jundger.work.pojo.Storage;
import com.jundger.work.pojo.Terminal;
import com.jundger.work.pojo.User;
import com.jundger.work.quartz.OvertimeJob;
import com.jundger.work.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/3/30 23:59
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userDao;

	@Resource
	private StorageMapper storageMapper;

	@Resource
	private CellMapper cellMapper;

	@Resource
	private TerminalMapper terminalMapper;

	private static Logger logger = Logger.getLogger(OvertimeJob.class);

	// 通过ID查询用户信息
	public User getUserById(int userId) {

		return this.userDao.selectByPrimaryKey(userId);
	}

	public User getUserByOpenId(String openid) {
		return this.userDao.selectByOpenId(openid);
	}

	public Storage getAllOrderById(int userId) {
		return this.storageMapper.selectOrderByUserId(userId);
	}

	public Storage getRunningOrderById(int userId) {
		return this.storageMapper.selectRunningOrderByUserId(userId);
	}

	public Cell getAvailableCell(Integer terminal_id, String type) {
		return this.cellMapper.selectAvailableCell(terminal_id, type);
	}

	public int addStorageOrder(Storage record) {
		return storageMapper.insertSelective(record);
	}

	public int updateCellStatus(Cell record) {
		return cellMapper.updateByPrimaryKeySelective(record);
	}

	public int overtimeReset(String order_no) {

		Storage storage = storageMapper.selectByOrderNo(order_no);
		if (storage != null && storage.getOrderStatus().equals(OrderStatusEnum.WAITING.toString())) {
			storageMapper.resetOvertimeOrder(order_no);
			logger.info("重置Overtime超时状态成功！");
			return 1;
		}
		logger.info(JSON.toJSONStringWithDateFormat(storage, "yyyy-MM-dd HH:mm:ss"));
		logger.info("重置Overtime超时状态失败！");
		return 0;
	}

	public int expireReset(String order_no) {

		Storage storage = storageMapper.selectByOrderNo(order_no);
		if (storage != null && storage.getOrderStatus().equals(OrderStatusEnum.RUNNING.toString())) {
			storageMapper.resetExpireOrder(order_no);
			logger.info("重置Expire到期状态成功！");
			return 1;
		}
		logger.info(JSON.toJSONStringWithDateFormat(storage, "yyyy-MM-dd HH:mm:ss"));
		logger.info("重置Overtime超时状态失败！");
		return 0;
	}

	public Storage getOrderForCheck(String order_no, String keyt, Integer terminal_id) {

		return storageMapper.selectOrderForCheck(order_no, keyt, terminal_id);
	}

	public int updateBeginStatus(String order_no) {
		return storageMapper.updateBeginStatus(order_no);
	}

	public List<Terminal> getTerminalList() {
		return terminalMapper.selectAll();
	}

	public List<Storage> queryOrder(String openid) {
		return storageMapper.selectByOpenId(openid);
	}

	public int finishOrder(String order_no) {

		Storage storage = storageMapper.selectByOrderNo(order_no);
		if (OrderStatusEnum.RUNNING.toString().equals(storage.getOrderStatus())) {
			// 更新storage表
			storage.setOrderStatus(OrderStatusEnum.FINISH.toString());
			storageMapper.updateByPrimaryKeySelective(storage);

			// 此情况下数据库中的触发器会自动更新cell表状态

			return 1;
		}
		return 0;
	}

}
