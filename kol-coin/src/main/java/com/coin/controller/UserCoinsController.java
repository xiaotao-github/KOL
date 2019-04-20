package com.coin.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.coin.comment.JsonUtils;
import com.coin.comment.TokenUtil;
import com.coin.dto.Response;
import com.coin.dto.UserCoins;
import com.coin.service.UserCoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/UserCoins")
public class UserCoinsController {
    @Autowired
    UserCoinsService userCoinsService;

    /***
     * 获取当前用户平台币余额
     * @param request
     * @return
     */
    @RequestMapping("/getUserCoins")
    @ResponseBody
    public Response getUserCoins(HttpServletRequest request){
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String user_id = jwt.getClaim("user_id").asString();
            try {
                UserCoins userCoins =  userCoinsService.getUserCoins(user_id);
                response = new Response("查询当前用户平台币余额",JsonUtils.objectToJson(userCoins),200);
                return response;
            }catch (Exception e){
                e.printStackTrace();
                response = new Response("系统错误，请稍后重试",null,400);
                return response;
            }
        }else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }

    /****
     * 此处有个问题就是未校验用户余额为负的情况
     * 需要加个事务处理，回滚
     * @param request
     * @param coins
     * @return
     */
    @RequestMapping("/updateCoins")
    @ResponseBody
    public Response updateCoins(HttpServletRequest request,int coins){
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            try {
                String user_id = jwt.getClaim("user_id").asString();
                UserCoins userCoins = userCoinsService.getUserCoins(user_id);
                //获取当前用户的旧余额
                int old_coins=userCoins.getUser_coins();
                //减少当前余额
                int new_coins=old_coins-coins;
                userCoins.setUser_coins(new_coins);
                Boolean b = userCoinsService.updateCoins(userCoins);
                if (b==true){
                    response = new Response("更新余额成功",null,200);
                    return response;
                }else {
                    response = new Response("更新余额失败",null,400);
                    return response;
                }
            }catch (Exception e){
                e.printStackTrace();
                response = new Response("系统错误，请稍后重试",null,400);
                return response;
            }
        }else {
            response = new Response("登录信息失效，请重新登录", null, 405);
            return response;
        }
    }
}
