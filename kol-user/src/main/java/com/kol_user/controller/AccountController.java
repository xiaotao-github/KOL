package com.kol_user.controller;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kol_user.comment.JsonUtils;
import com.kol_user.comment.TokenUtil;
import com.kol_user.dto.Response;
import com.kol_user.service.GetTokenService;
import com.kol_user.service.UserService;
import com.kol_user.service.loginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    loginService loginService;
    @Autowired
    UserService userService;
    @Autowired
    GetTokenService getTokenService;

    /***
     * 获取登录验证码
     * @param telephone
     */
    @RequestMapping("/smscode")
    @ResponseBody
    public Response smscode(@RequestParam String telephone){
        Response response;
        try {
            loginService.getUserRdnum(telephone);
            response=new Response("验证码发送成功，5分钟后验证码失效",null,200);
        }catch (Exception e){
            e.printStackTrace();
            response=new Response("系统错误，请稍后重试",null,400);
        }
        return response;
    }


    /***
     * 登录
     * @param type
     * @param telephone
     * @param password
     * @param tid
     * @param tnickname
     * @param tavatar
     * @param userRdnum
     * @return
     * @throws Exception
     */
    @RequestMapping(value ="/login",method = {RequestMethod.POST})
    @ResponseBody
    public String login(String type,@RequestParam(required = false) String telephone,@RequestParam(required = false) String password, @RequestParam(required = false) String tid,
                        @RequestParam(required = false) String tnickname, @RequestParam(required = false) String tavatar,
                        @RequestParam(required = false) String userRdnum ,@RequestParam(required = false) String gender) {
        JSONObject jsonObject=new JSONObject();
        try {
            if (type.equals("1")){
                //        手机密码登录
                String res=loginService.loginByphoneAndpassword(telephone,password);
                return res;
            }else if (type.equals("2")){
                //        手机+验证码登录
                String res=loginService.RdNumLogin(telephone,userRdnum);
                return res;
            }else if (type.equals("3")){
                //        微信登录
                String res= loginService.loginByOther(tid, tnickname, tavatar,gender,type);
                return res;
            }else if (type.equals("4")){
                //        QQ登录
                String res = loginService.loginByOther(tid, tnickname, tavatar,gender,type);
                return res;
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("data",null);
            jsonObject.put("status",400);
            jsonObject.put("msg","系统异常，请稍后重试");
        }
        jsonObject.put("data",null);
        jsonObject.put("status",301);
        jsonObject.put("msg","错误的登录方式");
        return jsonObject.toJSONString();
    }

    //退出登录
    @RequestMapping(value = "/logout",produces = "application/json;charset=UTF-8",method = {RequestMethod.POST})
    @ResponseBody
    public Response logout(HttpServletRequest request){
        Response response;
        DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
        if(jwt!=null) {
            String user_id = jwt.getClaim("user_id").asString();
            Boolean isTrue = loginService.logout(user_id);
            if (isTrue) {
                response = new Response("用户退出登录成功", null, 200);
            }else {
                response = new Response("系统错误，请稍后重试", null, 400);
            }
        }else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }


    //批量获取用户信息
    @RequestMapping(value = "/infos",method = {RequestMethod.POST})
    @ResponseBody
    public Response infos(HttpServletRequest request,String[] uids) {
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            if (jwt != null) {
                List list = userService.findInfosByListId(uids);
                response = new Response("批量获取用户信息", JsonUtils.objectToJson(list), 200);
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试", null, 400);
        }
        return response;
    }
}
