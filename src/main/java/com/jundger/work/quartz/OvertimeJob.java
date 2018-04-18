package com.jundger.work.quartz;

import com.jundger.work.constant.Consts;
import com.jundger.work.service.UserService;
import com.jundger.work.serviceimpl.UserServiceImpl;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/17 22:00
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Component
@DisallowConcurrentExecution
public class OvertimeJob implements Job {

	@Autowired
	private UserService userService;

	private static Logger logger = Logger.getLogger(OvertimeJob.class);

	public OvertimeJob() {
	}

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		// 在Quartz中使用@Autowired或是@Resource时需要下列代码
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		logger.info("*****************Quartz任务被触发于： " + new Date() + "************");

		String order_no = jobExecutionContext.getJobDetail().getJobDataMap().getString(Consts.JOB_MAP_NAME);

		if (order_no != null) {
			logger.info("Overtime order_no --> " + order_no);

			// 取消订单，即将订单状态由WAITING-->OVERTIME，并取消储物格预订状态
			// 先判断订单状态是否为WAITING
			userService.overtimeReset(order_no);
		}
	}
}
