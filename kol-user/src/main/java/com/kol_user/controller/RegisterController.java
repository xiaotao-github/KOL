package com.kol_user.controller;


import com.kol_user.dto.Response;
import com.kol_user.dto.User;
import com.kol_user.service.GetTokenService;
import com.kol_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Controller
@RequestMapping("/account")
public class RegisterController {
    @Autowired
    com.kol_user.service.loginService loginService;
    @Autowired
    UserService userService;
    @Autowired
    GetTokenService getTokenService;

    /***
     * 获取注册验证码
     * @param telephone
     */
    @RequestMapping("/giveRegisterMessageService")
    @ResponseBody
    public void giveRegisterMessageService(@RequestParam String telephone){
        loginService.getRdNum(telephone);
    }

    /**
     * 注册
      * @param telephone
     * @param rdNum
     * @param password
     * @return
     * @throws SQLException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping("/register")
    @ResponseBody
    public Response register(String telephone, String rdNum, @RequestParam(required = false)String password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        Response response;
        try{
            //校验手机号是否被注册
            Boolean isTrue = userService.checkTelephone(telephone);
            if (isTrue){//为true，表示可以注册
                boolean isTure=loginService.isRdNum(telephone,rdNum);
                if (isTure){
                    //登录成功/校验成功
//            进行存储操作
                    //对密码进行MD5加密
                    loginService.register(telephone, password);
                    // userService.addTelephone(telephone, password);
                    response=new Response("注册成功",null,200);
                    return response;
                }
                response=new Response("验证码错误",null,302);
                return response;
            }
            response=new Response("手机已被注册，请直接登录",null,303);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response=new Response("系统错误，请稍后重试",null,400);
            return response;
        }
    }


    // /***
    //  *重置密码开始
    //  */
    // //   1、 忘记密码，先根据手机发送验证码，然后再进行校验
    // public boolean isEqualRdnum(String telephone, String rdNum){
    //     boolean isTrue=loginService.isRdNum(telephone,rdNum);
    //     if (isTrue){
    //     return true;
    //     }
    //     return false;
    // }
//    2、忘记密码的第二步，接受客户端再次发送过来的手机号码和密码
    @RequestMapping("/updatePassword")
    @ResponseBody
    public Response forgetPassword(@RequestParam String telephone, @RequestParam String password,@RequestParam String rdNum) throws SQLException {
        Response response;
        try {
            boolean isTrue=loginService.isRdNum(telephone,rdNum);
            if (isTrue){
                //        根据手机号来存储这个手机号码和密码
                User user = userService.findOneByTelephone(telephone);
                user.setPassword(password);
                userService.updateImformation(user);
                response=new Response("密码修改成功",null,200);
                return response;
            }else {
                response=new Response("验证码错误",null,302);
                return response;
            }
        }catch (Exception e){
            e.printStackTrace();
            response=new Response("系统错误，请稍后重试",null,400);
            return response;
        }
    }
    /***
     *重置密码结束
     */
}
