package com.kol_friends.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.kol_friends.comment.JsonUtils;
import com.kol_friends.comment.TokenUtil;
import com.kol_friends.dto.Response;
import com.kol_friends.service.IntimacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/Intimacy")
public class IntimacyController {
    @Autowired
    IntimacyService intimacyService;

    /**
     * 获取两个用户之间的亲密值
     * @param intimate_user_id
     * @return
     */
    @RequestMapping("/getIntimacyValue")
    @ResponseBody
    public Response getIntimacyValue(HttpServletRequest request, String intimate_user_id){
        //获取token
        DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
        String user_id = jwt.getClaim("user_id").asString();
        String intimacyValue = intimacyService.getIntimacyValue(user_id, intimate_user_id);
        if (intimacyValue!=null && intimacyValue.length()!= 0){
            Response response = new Response("获取两个用户之间的亲密值", intimacyValue, 200);
            return response;
        }else {
            Response response = new Response("系统异常，请稍后重试", null, 400);
            return response;
        }
    }

    /***
     * 通过礼物增加亲密值（未完成，需调用礼物模块,获取送出的礼物值）
     * @param intimate_user_id
     * @return
     */
    @RequestMapping("/byGift")
    @ResponseBody
    public Response byGift(HttpServletRequest request,String intimate_user_id,Integer value){
        Response response;
        //获取token
        DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
        String user_id = jwt.getClaim("user_id").asString();
        response = intimacyService.byGift(user_id, intimate_user_id, value);
        return response;
    }

    // /**
    //  * 通过聊天增加亲密值（未完成，需调用房间模块）
    //  * @param user_id
    //  * @param intimate_user_id
    //  * @return
    //  */
    // @RequestMapping("/byChat")
    // @ResponseBody
    // public String byChat(String user_id,String intimate_user_id){
    //     intimacyService.byChat(user_id, intimate_user_id);
    //     return "聊天时长到达30分钟，亲密值增加";
    // }
}
