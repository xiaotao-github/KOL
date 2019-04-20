package com.kol_friends.controller;


import com.kol_friends.comment.JsonUtils;
import com.kol_friends.dto.Response;
import com.kol_friends.service.MessageBackstageService;
import com.kol_friends.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageListController {
    @Autowired
    MessageBackstageService messageBackstageService;
    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/getMessageList")
    @ResponseBody
    public Response getMessageList(HttpServletRequest request) {
        Response response;
        try {
            List list = messageBackstageService.getMessageList();
            response = new Response("获取消息推送列表成功", JsonUtils.objectToJson(list),200);
        } catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试",null,400);
        }
        return response;
    }


    //发送全体消息
    @RequestMapping(value = "/sendMessageToAll")
    @ResponseBody
    public Response sendMessageToAll(HttpServletRequest request) {
        Response response;
        try {
            messageService.sendMessageToAll();
            response = new Response("全体消息发送成功", null,200);
        } catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试",null,400);
        }
        return response;
    }
}
