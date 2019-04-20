package com.home.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.home.comment.JsonUtils;
import com.home.comment.TokenUtil;
import com.home.dto.Response;
import com.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/page")
public class FlashViewController {
    @Autowired
    HomeService homeService;


    @RequestMapping("/flashView")
    @ResponseBody
    public Response flashView(HttpServletRequest request){
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        try {
            if (jwt != null) {
                List list = homeService.flashView();
                response = new Response("首页轮播图查询成功", JsonUtils.objectToJson(list), 200);
            }else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        }catch (Exception e){
            response = new Response("系统异常，请稍后重试", null, 400);
        }
        return response;
    }
}
