package com.kol_user.controller;



import com.auth0.jwt.interfaces.DecodedJWT;
import com.kol_user.comment.JsonUtils;
import com.kol_user.comment.TokenUtil;
import com.kol_user.dto.Response;
import com.kol_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
@RequestMapping("/User")
public class SettingController {
    @Autowired
    com.kol_user.service.loginService loginService;
    @Autowired
    UserService userService;


//    1)	未绑定过手机的情况下，输入手机号码+短信验证码，设置登录密码，即可绑定手机
//    2)	已绑定情况下可进行换绑，需要输入当前绑定的手机+短信验证码进行换绑
   @RequestMapping(value = "/bind_Phone",method = {RequestMethod.POST})
   @ResponseBody
   public Response bindingPhone(HttpServletRequest request, String telephone, String userRdnum) throws SQLException {
       Response response;
       try {
           DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
           if(jwt!=null) {
               // try {
               String user_id = jwt.getClaim("user_id").asString();
               boolean isRdNum = loginService.isRdNum(telephone, userRdnum);
               if (isRdNum) {
                   //判断此手机号是否已经存在数据库中
                   boolean b = userService.checkTelephone(telephone);
                   //为true，表示可以注册或绑定
                   if (b){
                       // 将手机号码添加入用户的数据库信息中
                       userService.updateTelephone(user_id, telephone);
                       response = new Response("绑定手机号成功", null, 200);
                   }else {
                       response = new Response("此手机号已被注册", null, 303);
                   }
               } else {
                   response = new Response("验证码不正确，绑定失败", null, 304);
               }
           }else {
               response = new Response("登录信息失效，请重新登录", null, 405);
           }
       }catch (Exception e){
           e.printStackTrace();
           response = new Response("系统错误，请稍后重试", null, 400);
       }
       return response;
   }


    //重置密码
    @RequestMapping(value = "/forgetPassword",method = {RequestMethod.POST})
    @ResponseBody
    public String forgetPassword(String telephone, String rdNum,@RequestParam(required = false)String password) throws SQLException {
        try {
            String res = userService.forgetPassword(telephone, rdNum, password);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            Response response = new Response("系统错误，请稍后重试",null,400);
            return JsonUtils.objectToJson(response);
        }
    }
}
