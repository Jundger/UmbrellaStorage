package com.jundger.work.controller;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.ToMap;
import com.jundger.work.constant.Consts;
import com.jundger.work.pojo.Terminal;
import com.jundger.work.pojo.User;
import com.jundger.work.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping(value = "terminal")
public class TerminalController {

	@Resource
	private UserService userService;

	private static Logger logger = Logger.getLogger(TerminalController.class);

	/**
	 * 根据地理位置获取附近设备列表
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @return 一定距离内的设备集合
	 */
	@ResponseBody
	@RequestMapping(value = "getlist")
	public Map<String, Object> getTerminalList(@RequestParam(value = "longitude") Float longitude,
											   @RequestParam(value = "latitude") Float latitude,
											   @RequestParam(value = "radius", required = false, defaultValue = "10") Float radius) {

		logger.info("=================获取附近设备列表接口调用==================");

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> list = userService.getTerminalList(longitude, latitude, radius.doubleValue());

			if (list != null) {
				logger.info("list size --> " + list.size());
				returnMsg.put("code", "1");
				returnMsg.put("msg", "SUCCESS");
				returnMsg.put("data", list);
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "NULL");
			}

		} catch (Exception e) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FAIL");
		}

		return returnMsg;
	}

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
