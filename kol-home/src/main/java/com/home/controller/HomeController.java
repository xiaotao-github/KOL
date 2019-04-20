package com.home.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.home.dto.Home;
import com.home.service.HomeService;
import com.home.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/Home")
public class HomeController {
    @Autowired
    HomeService homeService;
    @Autowired
    IUserService userService;

    @RequestMapping("/showPage")
    public String test(){
        return "/home";
    }
    @RequestMapping("/login")
    public String login(){
        return "/login";
    }
    @RequestMapping("/error")
    public String error(){
        return "/error";
    }


    //    登录
    @RequestMapping(value = "/loginIn", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public ModelAndView  loginIn(@RequestParam String username,@RequestParam String password,
                          HttpServletRequest request){
        try {
            if (username!=null && ! username.trim().equals("") && password!=null && !password.trim().equals("")){
                UsernamePasswordToken token=new UsernamePasswordToken(username,password);
                //使用权限工具进行用户登录，登录成功后跳到shiro配置的successUrl中，与下面的return没什么关系！
                SecurityUtils.getSubject().login(token);
                // 将当前用户信息放入session
                SecurityUtils.getSubject().getSession().setAttribute("user",userService.findByName(username));
                return new ModelAndView("/home");
            }else {
                return new ModelAndView("/login").addObject("message","用户名密码不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ModelAndView("/login").addObject("message", "用户不存在");
        }
    }


    //显示主要内容
    @RequestMapping(value ="/findByProp", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByProp(){
        Map map=new HashMap();
        List list= homeService.findByProp(map);
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
    @RequestMapping(value = "/doInsert",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String doInsert(Home home, HttpServletRequest request) throws IOException {
        String s1 = "G";
        String id = s1.concat((String.valueOf((Math.random()*9+1)*10000)).substring(0,5));
        home.setId(id);
        //上传图片
        System.out.println(request.getParameter("name"));
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
        // String localPath = "D:/kol-photo/upload/home/";
        String localPath = "/usr/local/kol/home_image/";
        //定义 文件名
        String filename = null;
        if (!home.getFile().isEmpty()) {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = home.getFile().getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            System.out.println(filename);
            //文件保存路径
            home.getFile().transferTo(new File(localPath + filename));
        }
        //把图片的相对路径保存至数据库
        sqlPath = filename;
        System.out.println(sqlPath);
        home.setWorks_image(sqlPath);
        System.out.println(home);
        homeService.add(home);
        System.out.println("插入数据成功,ID为"+ home.getId());
        return "redirect:/Home/showPage";
    }

    //删除
     @RequestMapping("/delete")
     public String delete(String[] id){
         homeService.delete(id);
         return "redirect:/Home/showPage";
     }

    //上线
    @RequestMapping(value = "/online", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String online(@RequestParam String id){
        Home home = homeService.findOneById(id);
        home.setState(1);
        homeService.update(home);
        return "redirect:/Home/showPage";
    }

    //下线
    @RequestMapping(value = "/offline", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String offline(@RequestParam String id){
        Home home = homeService.findOneById(id);
        home.setState(2);
        homeService.update(home);
        return "redirect:/Home/showPage";
    }

    //编辑
    @RequestMapping(value = "/editing", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String editing(Home home,HttpServletRequest request) throws IOException {
        //上传图片
        System.out.println(request.getParameter("name"));
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
        // String localPath = "D:/kol-photo/upload/home/";
        String localPath = "/usr/local/kol/home_image/";
        //定义 文件名
        String filename = null;
        if (!home.getFile().isEmpty()) {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = home.getFile().getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            System.out.println(filename);
            //文件保存路径
            home.getFile().transferTo(new File(localPath + filename));
        }
        //把图片的相对路径保存至数据库
        sqlPath = filename;
        System.out.println(sqlPath);
        home.setWorks_image(sqlPath);
        System.out.println(home);
        homeService.update(home);
        return "redirect:/Home/showPage";
    }

    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) String id,
                              @RequestParam(required=false) String content_title,
                              @RequestParam(required=false) Integer type,
                              @RequestParam(required=false) Integer position,
                              @RequestParam(required=false) Integer state,
                              @RequestParam(required=false) String issuer
                              ){
        Map map=new HashMap();
        if (id==""){
            id=null;
        }else {
            map.put("id",id.trim());
        }
        if (content_title=="") {
            content_title=null;
        }else {
            map.put("content_title",content_title.trim());
        }
        if (type!=null) {
            map.put("type",type);
        }
        if (position!=null) {
            map.put("position",position);
        }
        if (state!=null) {
            map.put("state",state);
        }
        if (issuer=="") {
            issuer=null;
        }else {
            map.put("issuer",issuer.trim());
        }
        List list= homeService.findByProp(map);
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

    /***
     * 上传文件接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload/headImg", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject upload(HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","上传成功");
        return jsonObject;
    }
}
