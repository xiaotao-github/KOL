package com.coin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coin.comment.JsonUtils;
import com.coin.comment.TokenUtil;
import com.coin.dto.Gift;
import com.coin.dto.Response;
import com.coin.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/Gift")
public class GiftController {
    @Autowired
    GiftService giftService;

    @RequestMapping("/showPage")
    public String test(){
        return "/gift";
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
        List list= giftService.findByProp(map);
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
    public String doInsert(Gift gift, HttpServletRequest request) throws IOException {
        String s1 = "Z";
        String id = s1.concat((String.valueOf((Math.random()*9+1)*10000)).substring(0,5));
        gift.setId(id);
        //上传图片
        System.out.println(request.getParameter("name"));
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
//        String localPath = "D:/kol-photo/upload/";
         String localPath = "/usr/local/kol/gift_image/";
        //定义 文件名
        String filename = null;

        if (!gift.getFile().isEmpty()) {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = gift.getFile().getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            System.out.println(filename);
            //文件保存路径
            gift.getFile().transferTo(new File(localPath + filename));
            //把图片的相对路径保存至数据库
            // sqlPath = filename;
            sqlPath = "http://129.204.52.213/gift_image/"+filename;
            System.out.println(sqlPath);
            gift.setImage(sqlPath);
            System.out.println(gift);
            giftService.add(gift);
            System.out.println("插入数据成功,ID为"+ gift.getId());
            return "redirect:/Gift/showPage";
        }else {
            //把图片的相对路径保存至数据库
            // sqlPath = filename;
            sqlPath = "";
            gift.setImage(sqlPath);
            System.out.println(gift);
            giftService.add(gift);
            System.out.println("插入数据成功,ID为"+ gift.getId());
            return "redirect:/Gift/showPage";
        }
    }
    //删除
     @RequestMapping("/delete")
     public String delete(String[] id){
         giftService.delete(id);
         return "redirect:/Gift/showPage";
     }

    //上线
    @RequestMapping(value = "/online", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String online(@RequestParam String id){
        Gift gift = giftService.findOneById(id);
        gift.setState(1);
        giftService.update(gift);
        return "redirect:/Gift/showPage";
    }

    //下线
    @RequestMapping(value = "/offline", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String offline(@RequestParam String id){
        Gift gift = giftService.findOneById(id);
        gift.setState(2);
        giftService.update(gift);
        return "redirect:/Gift/showPage";
    }

    //编辑
    @RequestMapping(value = "/editing", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String editing(Gift gift,HttpServletRequest request) throws IOException {
        String id = gift.getId();
        //上传图片
        System.out.println(request.getParameter("name"));
        //保存数据库的路径
        String sqlPath = null;
        //定义文件保存的本地路径
//        String localPath = "D:/kol-photo/upload/";
         String localPath = "/usr/local/kol/gift_image/";
        //定义 文件名
        String filename = null;
        if (!gift.getFile().isEmpty()) {
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = gift.getFile().getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //得到 文件名
            filename = uuid + "." + suffixName;
            System.out.println(filename);
            //文件保存路径
            gift.getFile().transferTo(new File(localPath + filename));
            //把图片的相对路径保存至数据库
            // sqlPath = filename;
            sqlPath = "http://129.204.52.213/gift_image/"+filename;
            System.out.println(sqlPath);
            gift.setImage(sqlPath);
            System.out.println(gift);
            giftService.update(gift);
            return "redirect:/Gift/showPage";
        }else {
            Gift gift1 = giftService.findOneById(id);
            gift.setImage(gift1.getImage());
            giftService.update(gift);
            return "redirect:/Gift/showPage";
        }
    }

    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) String id,
                              @RequestParam(required=false) String gift_name,
                              @RequestParam(required=false) Integer state
                              ){
        Map map=new HashMap();
        if (id==""){
            id=null;
        }else {

            map.put("id",id.trim());
        }
        if (gift_name=="") {
            gift_name=null;
        }else {
            map.put("gift_name",gift_name.trim());
        }
        if (state!=null) {
            map.put("state",state);
        }
        List list= giftService.findByProp(map);
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

    //    获得房间内礼物列表
    @RequestMapping("/getGiftListToRoom")
    @ResponseBody
    public Response getGiftListToRoom(HttpServletRequest request){
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            if (jwt != null) {
                List list = giftService.getGiftListToRoom();
                response = new Response("礼物列表获取成功", JsonUtils.objectToJson(list), 200);
            }else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }
}
