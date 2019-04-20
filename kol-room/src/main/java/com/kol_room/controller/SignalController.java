package com.kol_room.controller;

import com.kol_room.service.SignalingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignalController {
    @Autowired
    SignalingService signalingService;
    @RequestMapping("/signalMsg")
    @ResponseBody
    public String signalingMsg(@RequestParam String accountName,@RequestParam String channelName){
        //登录
        try {
            signalingService.login(accountName);
            //加入频道
            signalingService.joinChannel(channelName,accountName);
            //频道发送消息
            signalingService.channelDeal("hhhh",accountName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
