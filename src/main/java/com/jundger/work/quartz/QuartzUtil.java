package com.jundger.work.quartz;

import com.jundger.work.constant.Consts;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/17 21:30
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class QuartzUtil {

	private Integer Ex_num = 0;
	private Integer Ov_num = 0;

	private static Logger log = Logger.getLogger(QuartzUtil.class);

	public void createExpireQuartz(String jobName, String triggerName, String data, long afterTime) {

		try {
			//初始化调度器工厂
			SchedulerFactory sf = new StdSchedulerFactory();
			//初始化调度器
			Scheduler sched = sf.getScheduler();

			// 获取当前时间的afterTime s之后
			long time=  System.currentTimeMillis() + (afterTime * 60 * 1000L);
			Date runTime = new Date(time);

			// 定义job
			// 在quartz中，有组的概念，组+job名称 唯一的
			JobDetail job = newJob(ExpireHandleJob.class)
					.withIdentity(jobName, Consts.JOB_EXPIRE_GROUP)
					.usingJobData(Consts.JOB_MAP_NAME, data)
					.build();

			//是否重复
			// SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(1);
			// 定义触发器，在下一分钟启动
			Trigger trigger = newTrigger().withIdentity(triggerName, Consts.TRIGGER_EXPIRE_GROUP).withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(runTime).build();

			// 将job注册到调度器
			sched.scheduleJob(job, trigger);

			log.info(job.getKey() + " 将会运行于: " + runTime);

			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void createOvertimeQuartz(String jobName, String triggerName, String data, long afterTime) {

		try {
			//初始化调度器工厂
			SchedulerFactory sf = new StdSchedulerFactory();
			//初始化调度器
			Scheduler sched = sf.getScheduler();

			// 获取当前时间的afterTime s之后
			long time=  System.currentTimeMillis() + (afterTime * 60 * 1000L);
			Date runTime = new Date(time);

			// 定义job
			// 在quartz中，有组的概念，组+job名称 唯一的
			JobDetail job = newJob(OvertimeJob.class)
					.withIdentity(jobName, Consts.JOB_OVERTIME_GROUP)
					.usingJobData(Consts.JOB_MAP_NAME, data)
					.build();

			//是否重复
			// SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(1);
			// 定义触发器，在下一分钟启动
			Trigger trigger = newTrigger().withIdentity(triggerName, Consts.TRIGGER_OVERTIME_GROUP).withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(runTime).build();

			// 将job注册到调度器
			sched.scheduleJob(job, trigger);

			log.info(job.getKey() + " 将会运行于: " + runTime);

			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public String getNewExpireJobName() {

		Ex_num %= 10000;

		return Consts.JOB_EXPIRE_NAME + String.valueOf(Ex_num++);
	}

	public String getNewExpireTriggerName() {

		Ex_num %= 10000;

		return Consts.TRIGGER_EXPIRE_NAME + String.valueOf(Ex_num);
	}

	public String getNewOvertimeJobName() {

		Ov_num %= 10000;

		return Consts.JOB_OVERTIME_NAME + String.valueOf(Ov_num++);
	}

	public String getNewOvertimeTriggerName() {

		Ov_num %= 10000;

		return Consts.TRIGGER_OVERTIME_NAME + String.valueOf(Ov_num);
	}

//	public String[] getNewExpireName() {
//
//		Ex_num %= 10000;
//		String suffix = String.valueOf(Ex_num++);
//		return new String[]{Consts.JOB_EXPIRE_NAME + suffix, Consts.TRIGGER_EXPIRE_NAME + suffix};
//	}
//
//	public String[] getNewOvertimeName() {
//
//		Ov_num %= 10000;
//		String suffix = String.valueOf(Ov_num++);
//		return new String[]{Consts.JOB_OVERTIME_NAME + suffix, Consts.TRIGGER_OVERTIME_NAME + suffix};
//	}
}
