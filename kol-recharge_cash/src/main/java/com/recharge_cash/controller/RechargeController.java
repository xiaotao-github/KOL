package com.recharge_cash.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.recharge_cash.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/Recharge")
public class RechargeController {
    @Autowired
    RechargeService rechargeService;

    @RequestMapping("/showPage")
    public String test(){
        return "/recharge";
    }

    //显示主要内容
    @RequestMapping(value ="/findByProp", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByProp(){
        Map map=new HashMap();
        List list= rechargeService.findByProp(map);
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("code", 0);
        obj.put("count",list.size());
        obj.put("msg", "");
        obj.put("data",list);
        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        return jsonObject;
    }


    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) String date,
                              @RequestParam(required=false) String id,
                              @RequestParam(required=false) String username,
                              @RequestParam(required=false) String order_number,
                              @RequestParam(required=false) Integer payment,
                              @RequestParam(required=false) Integer state
                              ){
        Map map=new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date==null||date.equals("")) {
            date=null;
        }else {
            String date1 = date.split(",")[0].trim();
            String date2 = date.split(",")[1].trim();
            try {
                Date startDate = sdf.parse(date1);
                Date endDate = sdf.parse(date2);
                map.put("startDate",startDate);
                map.put("endDate",endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (id=="") {
            id=null;
        }else {
            map.put("id",id.trim());
        }
        if (order_number=="") {
            order_number=null;
        }else {
            map.put("order_number",order_number.trim());
        }
        if (username=="") {
            username=null;
        }else {
            map.put("username",username.trim());
        }
        if (payment!=null) {
            map.put("payment",payment);
        }
        if (state!=null) {
            map.put("state",state);
        }
        List list= rechargeService.findByProp(map);
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
}
