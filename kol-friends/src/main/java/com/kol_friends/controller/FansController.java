package com.kol_friends.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kol_friends.comment.JsonUtils;
import com.kol_friends.comment.TokenUtil;
import com.kol_friends.dto.Fans;
import com.kol_friends.dto.Response;
import com.kol_friends.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/Fans")
public class FansController {
    @Autowired
    FansService fansService;

    /***
     * 返回粉丝数量
     * @param request
     * @return
     */
    @RequestMapping("/getFansNumber")
    @ResponseBody
    public Response getFansNumber(HttpServletRequest request) {
        Response response;
        try {
            //获取token
            DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
            String user_id = jwt.getClaim("user_id").asString();
            List list = fansService.getFansList(user_id);
            String fansNumber = String.valueOf(list.size());
            response = new Response("用户的粉丝数量", fansNumber, 200);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试", null, 400);
        }
        return response;
    }


    /***
     * 根据搜索条件返回粉丝列表
     * @param request
     * @param keyword
     * @return
     */
    @RequestMapping("/getFansListByWord")
    @ResponseBody
    public Response getFansListByWord(HttpServletRequest request, @RequestParam(defaultValue = "", required = false) String keyword
            , @RequestParam(defaultValue = "1", required = true, value = "pageNo") Integer pageNo) {
        Response response;
        try {
            //获取token
            DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
            Integer pageSize = 10;
            Page<Object> objects = PageHelper.startPage(pageNo, pageSize);
            System.out.println("pages1====" + objects.getPages());
            String user_id = jwt.getClaim("user_id").asString();
            List list = fansService.getFansListByWord(user_id, keyword);
//            PageInfo pageInfo = new PageInfo<>(list);
            System.out.println("pages2====" + objects.getPages());
            if (pageNo <= objects.getPages()) {
                response = new Response("查询粉丝列表成功", JsonUtils.objectToJson(list), 200);
            } else {
                response = new Response("查询粉丝列表结束", "[]", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试", null, 400);
        }
        return response;
    }

}
