package com.jundger.work.constant;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/17 16:51
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public enum OrderStatusEnum {

	// 创建订单成功后，等待用户使用服务
	WAITING,

	// 生成订单后未使用服务超过一定时间后终止订单
	OVERTIME,

	// 用户已从目标终端使用服务（申请服务时长内）
	RUNNING,

	// 超过申请时长后未取走/还回
	EXPIRE,

	// 服务正常结束
	FINISH
}
