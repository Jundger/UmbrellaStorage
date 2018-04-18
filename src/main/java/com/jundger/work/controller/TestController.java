package com.jundger.work.controller;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.ToMap;
import com.jundger.work.pojo.User;
import com.jundger.work.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/3/30 23:51
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

@Controller
@RequestMapping(value = "test")
public class TestController {

	@Resource
	private UserService userService;

	private static Logger logger = Logger.getLogger(TestController.class);

	@ResponseBody
	@RequestMapping(value = "{id}/showUsers", produces = "application/json; charset=utf-8")
	public String test(HttpServletRequest request, Model model, @PathVariable String id) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		logger.info("TestController request comming!");

		User user;
		try {
			user = this.userService.getUserById(Integer.valueOf(id));
			logger.info(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			logger.error("数据库读取失败！");
			returnMsg.put("return_code", "2");
			returnMsg.put("msg", "UNKNOWN_ERROR");
			return ToMap.MapToJson(returnMsg);
		}

		logger.info("数据库读取成功！");

		returnMsg.put("nickname", user.getNickname());
		returnMsg.put("openid", user.getOpenId());
		returnMsg.put("return_code", "1");
		returnMsg.put("msg", "SUCCESS");

		return ToMap.MapToJson(returnMsg);
	}
}
