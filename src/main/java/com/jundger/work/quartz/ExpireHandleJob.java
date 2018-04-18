package com.jundger.work.quartz;

import com.jundger.work.constant.Consts;
import com.jundger.work.service.UserService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/17 19:02
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Component
@DisallowConcurrentExecution
public class ExpireHandleJob implements Job {

	@Autowired
	private UserService userService;

	private static Logger logger = Logger.getLogger(ExpireHandleJob.class);

	public ExpireHandleJob() {
	}

	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		logger.info("*****************Quartz任务被触发于： " + new Date() + "************");

		// 在Quartz中使用@Autowired或是@Resource时需要下列代码
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		String order_no = jobExecutionContext.getJobDetail().getJobDataMap().getString(Consts.JOB_MAP_NAME);

		if (order_no != null) {
			logger.info("Expire order_no --> " + order_no);

			// 到期后检查是否订单完成，若未完成则将状态由RUNNING-->EXPIRE
			// 并重置储物格状态：USING-->EXPIRE, book_flag置为0
			userService.expireReset(order_no);
		}
	}
}
