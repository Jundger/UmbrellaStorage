package com.jundger.work.service;

import com.jundger.work.pojo.Cell;
import com.jundger.work.pojo.Storage;
import com.jundger.work.pojo.User;

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
	public User getUserById(int userId);

	// 通过openid查询用户信息
	public User getUserByOpenId(String openid);

	// 通过id查询用户订单
	public Storage getAllOrderById(int userId);

	// 通过id查询用户未完成的订单
	public Storage getRunningOrderById(int userId);

	// 通过terminal_id查询储物格信息
	public Cell getAvailableCell(Integer terminal_id);

	// 新增订单信息
	public int addStorageOrder(Storage record);

	// 更新储物格使用状态
	public int updateCellStatus(Cell record);

	// 超时后重置订单状态
	public int overtimeReset(String order_no);

	// 到期后重置订单状态
	public int expireReset(String order_no);

	// 检验从终端设备传递来的数据正确性
	public Storage getOrderForCheck(String order_no, String keyt, Integer terminal_id);

	// 订单开始后重置一些状态
	int updateBeginStatus(String order_no);

}
