package com.kol_friends.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kol_friends.comment.JsonUtils;
import com.kol_friends.service.MessageBackstageService;
import com.kol_friends.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/Message")
public class MessageBackstageController {
    @Autowired
    MessageBackstageService messageBackstageService;

    @RequestMapping("/showPage")
    public String test(){
        return "/message";
    }
    @RequestMapping("/test")
    public String test1(){
        return "/test";
    }

    //显示主要内容
    @RequestMapping(value ="/findByProp", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByProp(){
        Map map=new HashMap();
        List list= messageBackstageService.findByProp(map);
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",list.size());
        obj.put("data",list);
        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        return jsonObject;
    }

    //新增数据
    @RequestMapping(value = "/doInsert",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String doInsert(Message message,HttpServletRequest request) throws Exception {
        //上传图片
        System.out.println(request.getParameter("name"));
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
        String localPath = "/usr/local/kol/message_image/";
        //定义 文件名
        String filename = null;
        if (!message.getFile().isEmpty()) {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = message.getFile().getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            System.out.println(filename);
            //文件保存路径
            message.getFile().transferTo(new File(localPath + filename));
            //把图片的相对路径保存至数据库
            sqlPath = "http://129.204.52.213/message_image/"+filename;
            System.out.println(sqlPath);
            message.setImage(sqlPath);
            System.out.println(message);
            messageBackstageService.add(message);
            System.out.println("插入数据成功,ID为"+ message.getId());
            return "redirect:/Message/showPage";
        }else {
            //把图片的相对路径保存至数据库
            sqlPath = "";
            System.out.println(sqlPath);
            message.setImage(sqlPath);
            System.out.println(message);
            messageBackstageService.add(message);
            System.out.println("插入数据成功,ID为"+ message.getId());
            return "redirect:/Message/showPage";
        }
    }


    //删除
     @RequestMapping("/delete")
     public String delete(int[] id){
         messageBackstageService.delete(id);
         return "redirect:/Message/showPage";
     }


    //上线
    @RequestMapping(value = "/online")
    public String online(@RequestParam Integer id){
        Message message = messageBackstageService.findOneById(id);
        message.setState(1);
        messageBackstageService.update(message);
        return "redirect:/Message/showPage";
    }

    //下线
    @RequestMapping(value = "/offline")
    public String offline(@RequestParam Integer id){
        Message message = messageBackstageService.findOneById(id);
        message.setState(2);
        messageBackstageService.update(message);
        return "redirect:/Message/showPage";
    }

    //编辑
    @RequestMapping(value = "/editing",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String editing(Message message,HttpServletRequest request) throws IOException {
        int id = message.getId();
        //上传图片
        System.out.println(request.getParameter("name"));
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
        String localPath = "/usr/local/kol/message_image/";
        //定义 文件名
        String filename = null;
        if (!message.getFile().isEmpty()) {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = message.getFile().getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            System.out.println(filename);
            //文件保存路径
            message.getFile().transferTo(new File(localPath + filename));
            //把图片的相对路径保存至数据库
            sqlPath = "http://129.204.52.213/message_image/"+filename;
            System.out.println(sqlPath);
            message.setImage(sqlPath);
            messageBackstageService.update(message);
            return "redirect:/Message/showPage";
        }else {
            Message message1 = messageBackstageService.findOneById(id);
            message.setImage(message1.getImage());
            messageBackstageService.update(message);
            return "redirect:/Message/showPage";
        }
    }

    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) Integer id,
                              @RequestParam(required=false) String title,
                              @RequestParam(required=false) Integer state
                              ){
        Map map=new HashMap();
        if (id!=null){
            map.put("id",id);
        }
        if (title=="") {
            title=null;
        }else {
            map.put("title",title.trim());
        }
        if (state!=null) {
            map.put("state",state);
        }
        List list= messageBackstageService.findByProp(map);
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",list.size());
        obj.put("data",list);
        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
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

    /***
     * 上传文件接口
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    @ResponseBody
    public String uploadPhoto(MultipartFile file) throws Exception {
        //上传图片
        //保存数据库的路径
        //定义文件保存的本地路径
//        String localPath = "D:/kol_photo/upload/";
        String localPath = "/usr/local/kol/message_image/";
        //定义 文件名
        String filename = null;
        if (file != null && !file.isEmpty()) {
            System.out.println("imageName====="+file.getName());
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = file.getContentType();
            System.out.println(contentType);
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            //文件保存路径
            file.transferTo(new File(localPath + filename));
        }
        Map map = new HashMap();
        Map map1 = new HashMap();
        map.put("code",0);
        map.put("msg","上传成功");
        //src保存的路径地址，title可以不写
        map1.put("src","http://129.204.52.213/message_image/"+filename);
        System.out.println(file);
        System.out.println(map1.get("src"));
        map1.put("title","1.png");
        map.put("data",map1);
        return JsonUtils.objectToJson(map);
    }

}
