package com.jundger.work.constant;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/17 16:33
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Consts {

	public static final Integer KEY_LENGTH = 16;

	public static final String TYPE_OF_STORAGE = "STORAGE";
	public static final String TYPE_OF_BORROW = "BORROW";

	/**
	 * 订单超时时间
	 * 单位：分钟
	 */
	public static final Integer ORDER_OVERTIME_TIME = 120;

	/**
	 * 附近设备搜索半径
	 * 单位：公里
	 */
	public static final Integer NEARBY_RADIUS = 2;

	public static final Integer CELL_BOOK_FLAG_OFF = 0;
	public static final Integer CELL_BOOK_FLAG_ON = 1;

	public static final String JOB_MAP_NAME = "order";

	public static final String JOB_EXPIRE_NAME = "Expire_Job";
	public static final String JOB_EXPIRE_GROUP = "Expire_Job_Group";
	public static final String TRIGGER_EXPIRE_NAME = "Expire_Trigger";
	public static final String TRIGGER_EXPIRE_GROUP = "Expire_Trigger_Group";

	public static final String JOB_OVERTIME_NAME = "Overtime_Job";
	public static final String JOB_OVERTIME_GROUP = "Overtime_Job_Group";
	public static final String TRIGGER_OVERTIME_NAME = "Overtime_Trigger";
	public static final String TRIGGER_OVERTIME_GROUP = "Overtime_Job_Group";
}
