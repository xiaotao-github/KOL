package com.kol_room.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kol_room.dto.Room;
import com.kol_room.service.RoomService;
import com.kol_room.service.RoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
//后台管理房间
@Controller
@RequestMapping("/Room")
public class RoomController {
    @Autowired
    RoomServiceImpl roomService;

        @RequestMapping("/showPage")
    public String test(){
        return "/room";
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
        JSONObject obj=new JSONObject();
        List list= roomService.findByProp(map);
        if (list!=null&&list.size()>0){
            //前台通过key值获得对应的value值
            obj.put("code", 0);
            obj.put("msg", "");
            obj.put("count",list.size());
            obj.put("data",list);
            //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
            String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
            return jsonObject;
        }else {
            obj.put("code", 400);
            obj.put("msg", "系统错误，请稍后重试");
            obj.put("data",null);
            String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
            return jsonObject;
        }
    }


    //关闭
     @RequestMapping("/close")
     public String close(String id){
         Room room = roomService.findOneById(id);
         room.setState(0);
         roomService.update(room);
         return "redirect:/Room/showPage";
     }
    //取消关闭
    @RequestMapping("/unclose")
    public String unclose(String id){
        Room room = roomService.findOneById(id);
        room.setState(1);
        roomService.update(room);
        return "redirect:/Room/showPage";
    }

    //置顶
    @RequestMapping(value = "/stick", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    public String stick(@RequestParam String id){
        Room room = roomService.findOneById(id);
        room.setRoom_position(0);
        roomService.update(room);
        return "redirect:/Room/showPage";
    }

    //取消置顶
    @RequestMapping(value = "/unstick", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    public String unstick(@RequestParam String id){
        Room room = roomService.findOneById(id);
        room.setRoom_position(1);
        roomService.update(room);
        return "redirect:/Room/showPage";
    }

    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) String id,
                              @RequestParam(required=false) String room_name,
                              @RequestParam(required=false) String username
                              ){
        Map map=new HashMap();
        if (id==""){
            id=null;
        }else {
            map.put("id",id.trim());
        }
        if (room_name=="") {
            room_name=null;
        }else {
            map.put("room_name",room_name.trim());
        }
        if (username=="") {
            username=null;
        }else {
            map.put("username",username.trim());
        }
        List list= roomService.findByProp(map);
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


    @RequestMapping("/update_RoomNotice")
    public void update_RoomNotice(@RequestParam String notice,@RequestParam String id){
        Room room=new Room();
        room.setRoom_notice(notice);
        room.setId(id);
        roomService.update(room);
    }
}
