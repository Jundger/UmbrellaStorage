package com.jundger.work.service;

import com.jundger.work.pojo.Cell;
import com.jundger.work.pojo.Storage;
import com.jundger.work.pojo.Terminal;
import com.jundger.work.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/3/31 0:02
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public interface UserService {

	// 通过ID查询用户信息
	User getUserById(int userId);

	// 通过openid查询用户信息
	User getUserByOpenId(String openid);

	// 插入用户信息
	int addUserSelective(User record);

	// 通过id查询用户订单
	Storage getAllOrderById(int userId);

	// 通过id查询用户未完成的订单
	Storage getRunningOrderById(int userId);

	// 通过terminal_id查询储物格信息
	Cell getAvailableCell(Integer terminal_id, String type);

	// 新增订单信息
	int addStorageOrder(Storage record);

	// 更新储物格使用状态
	int 	updateCellStatus(Cell record);

	// 超时后重置订单状态
	int overtimeReset(String order_no);

	// 到期后重置订单状态
	int expireReset(String order_no);

	// 检验从终端设备传递来的数据正确性
	Storage getOrderForCheck(String order_no, String keyt, Integer terminal_id);

	// 订单开始后重置一些状态
	int updateBeginStatus(String order_no);

	// 获取一定范围内的设备列表
	List<Map<String, Object>> getTerminalList(Float longitude, Float latitude, Double radius);

	// 根据openid查询用户所有订单
	List<Storage> queryOrder(String openid);

	// 取消订单
	String cancelOrder(String order_no);

	// 订单正常结束
	String finishOrder(Storage storage);
}
