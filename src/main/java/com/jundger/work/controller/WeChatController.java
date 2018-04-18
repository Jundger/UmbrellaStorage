package com.jundger.work.controller;

import com.jundger.common.util.ToMap;
import com.jundger.work.util.MessageUtil;
import com.jundger.work.util.SignUtil;
import com.jundger.work.wechat.Consts;
import com.jundger.work.wechat.EventDispatcher;
import com.jundger.work.wechat.MsgDispatcher;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/13 1:03
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Controller
@RequestMapping(value = "subscription")
public class WeChatController {
	private static Logger logger = Logger.getLogger(WeChatController.class);

	@RequestMapping(value = "jump", method = RequestMethod.POST)
	public void toApp(HttpServletRequest request, HttpServletResponse response) {

	}

	@RequestMapping(value = "validate", method = RequestMethod.GET)
	public void validateCount(HttpServletRequest request, HttpServletResponse response,
							  @RequestParam(value = "signature") String signature,
							  @RequestParam(value = "timestamp") String timestamp,
							  @RequestParam(value = "nonce") String nonce,
							  @RequestParam(value = "echostr") String echostr) {
		logger.info("WeChat primary check comming!");

		try {
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				PrintWriter out = response.getWriter();
				out.print(echostr);
				out.close();
				logger.info("微信服务器校验成功！");
			} else {
				logger.info("存在非法请求！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
	}

	@RequestMapping(value = "validate", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String returnMsg;
		logger.info("收到来自微信服务器的POST请求");
		try {
			Map<String, String> map = MessageUtil.parseXml(request);
			logger.info(map.toString());
			String msgtype = map.get("MsgType");
			if (Consts.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)) {
				returnMsg = EventDispatcher.processEvent(map); //进入事件处理
			} else {
				returnMsg = MsgDispatcher.processMessage(map); //进入消息处理
			}
			logger.info("返回数据 -->" + returnMsg);
			PrintWriter out = response.getWriter();
			out.print(returnMsg);
			out.close();
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
