package com.kol_friends.service;

import com.kol_friends.dao.MessageBackstageDao;
import io.rong.RongCloud;
import io.rong.example.message.MessageExample;
import io.rong.messages.TxtMessage;
import io.rong.methods.message.system.MsgSystem;
import io.rong.models.message.SystemMessage;
import io.rong.models.message.TemplateMessage;
import io.rong.models.response.ResponseResult;
import io.rong.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/***
 * 发送系统消息
 */
@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    MessageBackstageDao messageBackstageDao;

    /**
     * 此处替换成您的appKey
     * */
    private static final String appKey = "kj7swf8ok3ir2";
    /**
     * 此处替换成您的appSecret
     * */
    private static final String appSecret = "3oFRQ7NWOd0Rh";

    /**
     * 发送者ID
     * */
    private static final String senderId = "Administrator";
    //文字消息（content文字消息的文字内容，包括表情；extra扩展信息，可以放置任意的数据内容，也可以去掉此属性。）
    // private static final TxtMessage txtMessage = new TxtMessage("你们已经成为好友，快来聊天吧", "helloExtra");

    private static final TxtMessage txtMessage = new TxtMessage("你关注的用户正在直播中，开来看看吧","");

    private static final TxtMessage txtMessage2 = new TxtMessage("全体消息发送成功","");

    /**
     * 自定义api地址
     * */
    private static final String api = "http://api.cn.ronghub.com";

    public void sendMessage(String user_id, String friends_id) throws Exception {
        String[] targetIds={user_id,friends_id};
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        //系统消息
        MsgSystem system = rongCloud.message.system;
        /**
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/message/system.html#send
         *
         * 发送系统消息
         *
         */
        SystemMessage systemMessage = new SystemMessage()
                //发送者ID
                .setSenderId(senderId)
                //接收ID
                .setTargetId(targetIds)
                .setObjectName(txtMessage.getType())
                .setContent(txtMessage)
                .setPushContent("this is a push")
                .setPushData("{'pushData':'hello'}")
                //针对融云服务端是否存储此条消息，客户端则根据消息注册的 ISPERSISTED 标识判断是否存储,0 表示为不存储、 1 表示为存储，默认为 1 存储消息
                .setIsPersisted(0)
                .setIsCounted(0)
                //针对 iOS 平台，对 SDK 处于后台暂停状态时为静默推送,1 表示为开启，0 表示为关闭，默认为 0（可选）
                .setContentAvailable(0);
        ResponseResult result = system.send(systemMessage);
        System.out.println("send system message:  " + result.toString()+systemMessage.getContent());
        /**
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/message/system.html#sendTemplate
         *
         * 发送系统模板消息方法
         *
         */
        Reader reader = null ;
        try {
            reader =new BufferedReader( new InputStreamReader(MessageExample.class.getClassLoader().getResourceAsStream("jsonsource/message/TemplateMessage.json")));
            TemplateMessage template = (TemplateMessage) GsonUtil.fromJson(reader, TemplateMessage.class);
            ResponseResult systemTemplateResult = system.sendTemplate(template);
            System.out.println("send system template message:  " + systemTemplateResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != reader){
                reader.close();
            }
        }
    }

    //发送全体消息
    public void sendMessageToAll() throws Exception {
        List list = messageBackstageDao.getUserIdList();
        String[] targetIds = {String.valueOf(list)};
        // String[] targetIds = (String[]) list.toArray(new String[list.size()]);
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        //系统消息
        MsgSystem system = rongCloud.message.system;
        /**
         * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/message/system.html#send
         *
         * 发送系统消息
         *
         */
        SystemMessage systemMessage = new SystemMessage()
                //发送者ID
                .setSenderId(senderId)
                //接收ID
                .setTargetId(targetIds)
                .setObjectName(txtMessage2.getType())
                .setContent(txtMessage2)
                .setPushContent("this is a push")
                .setPushData("{'pushData':'hello'}")
                //针对融云服务端是否存储此条消息，客户端则根据消息注册的 ISPERSISTED 标识判断是否存储,0 表示为不存储、 1 表示为存储，默认为 1 存储消息
                .setIsPersisted(0)
                .setIsCounted(0)
                //针对 iOS 平台，对 SDK 处于后台暂停状态时为静默推送,1 表示为开启，0 表示为关闭，默认为 0（可选）
                .setContentAvailable(0);
        ResponseResult result = system.send(systemMessage);
        System.out.println("发送全体消息" + result.toString() + systemMessage.getContent());
    }

}
