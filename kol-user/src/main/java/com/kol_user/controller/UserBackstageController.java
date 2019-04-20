package com.kol_user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kol_user.service.UserBackstageService;
import com.kol_user.service.UserService;
import com.kol_user.service.UserServiceImpl;
import com.kol_user.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/User")
public class UserBackstageController {
    @Autowired
    UserBackstageService userBackstageService;

    /***
     * 返回用户管理首页
     * @return
     */
    @RequestMapping("/showPage")
    public String showPage(){
        return "/user";
    }

    /**
     * 返回后台管理主页
     * @return
     */
    @RequestMapping("/home")
    public String home(){
        return "/home";
    }

    /***
     * 后台登录界面
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String login(){
        return "/login";
    }



    //显示主要内容
    @RequestMapping(value ="/findByProp", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByProp(){
        Map map=new HashMap();
        List list=userBackstageService.findByProp(map);
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",list.size());
        obj.put("data",list);
        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonObject;
    }

    //新增数据
    @RequestMapping(value = "/doInsert",method = RequestMethod.POST)
    public String doInsert(User user, HttpServletRequest request) throws Exception {
        System.out.println(user);
        user.setUser_status(1);
        userBackstageService.add(user);
        System.out.println("插入数据成功,ID为"+user.getId());
        return "redirect:/User/showPage";
    }



    //删除
     @RequestMapping("/delete")
     public String delete(String[] id){
         userBackstageService.delete(id);
         return "redirect:/User/showPage";
     }


    //禁言
    @RequestMapping(value = "/shutup")
    public String shutup(@RequestParam String id){
        User user = userBackstageService.findOneById(id);
        // user.setId(id);
        user.setState(2);
        userBackstageService.update(user);
        return "redirect:/User/showPage";
    }
//, method = RequestMethod.POST,produces = "text/json;charset=UTF-8"
    //冻结
    @RequestMapping(value = "/freeze")
    public String freeze(@RequestParam String id){
        User user = userBackstageService.findOneById(id);
        // user.setId(id);
        user.setState(3);
        userBackstageService.update(user);
        return "redirect:/User/showPage";

    }

    //封号
    @RequestMapping(value = "/ban")
    public String ban(@RequestParam String id){
        User user = userBackstageService.findOneById(id);
        // user.setId(id);
        user.setState(4);
        userBackstageService.update(user);
        return "redirect:/User/showPage";

    }

    //解除（正常状态）
    @RequestMapping(value = "/relieve")
    public String relieve(@RequestParam String id){
        User user = userBackstageService.findOneById(id);
        user.setState(1);
        userBackstageService.update(user);
        return "redirect:/User/showPage";

    }

    //编辑
    @RequestMapping(value = "/update")
    public String update(User user){
        userBackstageService.update(user);
        return "redirect:/User/showPage";
    }

    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) String id,
                              @RequestParam(required=false) String username,
                              @RequestParam(required=false) String sex,
                              @RequestParam(required=false) Integer user_status,
                              @RequestParam(required=false) Integer state
                              ){
        Map map=new HashMap();
        if (id==""){
            id=null;
        }else{
            map.put("id",id.trim());
        }
        if (username=="") {
            username=null;
        }else {
            map.put("username",username.trim());
        }
        if (sex=="") {
            sex=null;
        }else {
            map.put("sex",sex);
        }
        if (user_status!=null) {
            map.put("user_status",user_status);
        }
        if (state!=null){
            map.put("state",state);
        }
        List list=userBackstageService.findByProp(map);
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",list.size());
        obj.put("data",list);
        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonObject;
    }
}
