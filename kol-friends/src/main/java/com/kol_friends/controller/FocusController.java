package com.kol_friends.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kol_friends.comment.JsonUtils;
import com.kol_friends.comment.TokenUtil;
import com.kol_friends.dto.Focus;
import com.kol_friends.dto.Response;
import com.kol_friends.service.FansService;
import com.kol_friends.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/Focus")
public class FocusController {
    @Autowired
    FocusService focusService;
    @Autowired
    FansService fansService;
    /***
     * 获取关注数量
     * @param request
     * @return
     */
    @RequestMapping("/getFocusNumber")
    @ResponseBody
    public Response getFocusNumber(HttpServletRequest request){
        Response response;
        try {
            //获取token
            DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
            String user_id = jwt.getClaim("user_id").asString();
            List list = focusService.getFocusList(user_id);
            String focusNumber = String.valueOf(list.size());
            response = new Response("用户的关注数量", focusNumber, 200);
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试",null,400);
        }
        return response;
    }



    /**
     * 添加和取消关注
     * @param request
     * @param focus_id
     * @param status
     * @return
     */
    @RequestMapping("/follow")
    @ResponseBody
    public Response follow(HttpServletRequest request,String focus_id,int status){
        Response response;
        //获取token
        try {
            DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
            String user_id = jwt.getClaim("user_id").asString();
            if (!focus_id.equals("") && !focus_id.equals(user_id)) {
                if (status == 0) {
                    //取消关注
                    focusService.cencelFocus(user_id, focus_id);
                    fansService.cancelFans(focus_id, user_id);
                    response = new Response("取消关注成功", null, 200);
                } else if (status == 1) {
                    //添加关注
                    focusService.addFocus(user_id, focus_id);
                    fansService.addFans(focus_id, user_id);
                    response = new Response("添加关注成功", null, 200);
                } else {
                    response = new Response("操作失败", null, 400);
                }
            }else {
                response = new Response("不可关注自己", null, 400);
            }
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

    /***
     * 根据搜索条件返回关注列表
     * @param request
     * @param keyword
     * @return
     */
    @RequestMapping("/getFocusListByWord")
    @ResponseBody
    public Response getFocusListByWord(HttpServletRequest request, @RequestParam(defaultValue="",required = false)String keyword){
        Response response;
        try{
            DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
            String user_id = jwt.getClaim("user_id").asString();
            List list = focusService.getFocusListByWord(user_id,keyword);
            response = new Response("查询关注列表成功",JsonUtils.objectToJson(list),200);
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试",null,400);
            return response;
        }
        return  response;
    }

}
