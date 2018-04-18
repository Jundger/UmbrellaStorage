package com.jundger.work.wechat;

import com.jundger.work.util.MessageUtil;
import com.jundger.work.wechat.response.TextMessage;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;

public class MsgDispatcher {
    private static Logger logger = Logger.getLogger(MsgDispatcher.class);

    public static String processMessage(Map<String, String> map) {
        if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
            logger.info("==============接收到文本消息！");
            String openid = map.get("FromUserName"); //用户 openid
            String mpid = map.get("ToUserName");   //公众号原始 ID
            String content = map.get("Content");

            //普通文本消息

            TextMessage txtmsg = new TextMessage();
            txtmsg.setToUserName(openid);
            txtmsg.setFromUserName(mpid);
            txtmsg.setCreateTime(new Date().getTime());
            txtmsg.setMsgType(Consts.RESP_MESSAGE_TYPE_TEXT);

            if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
                txtmsg.setContent(content);
                return MessageUtil.textMessageToXml(txtmsg);
            }
        }

        if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_IMAGE)) { // 图片消息
            logger.info("==============接收到图片消息！");
        }

        if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
            logger.info("==============接收到链接消息！");
        }

        if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_LOCATION)) { // 位置消息
            logger.info("==============接收到位置消息！");
        }

        if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_VIDEO)) { // 视频消息
            logger.info("==============接收到视频消息！");
        }

        if (map.get("MsgType").equals(Consts.REQ_MESSAGE_TYPE_VOICE)) { // 语音消息
            logger.info("==============接收到语音消息！");
        }

        return null;
    }
}
