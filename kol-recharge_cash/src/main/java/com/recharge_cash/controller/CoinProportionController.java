package com.recharge_cash.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.recharge_cash.dto.CoinProportion;
import com.recharge_cash.service.CoinProportionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/CoinProportion")
public class CoinProportionController {
    @Autowired
    CoinProportionService coinProportionService;

    @RequestMapping("/showPage")
    public String test(){
        return "/coinProportion";
    }


    //显示主要内容
    @RequestMapping(value ="/findByProp", method = RequestMethod.GET,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByProp(){
        Map map=new HashMap();
        List list= coinProportionService.findByProp(map);
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
    public String doInsert(CoinProportion coinProportion) throws Exception {
        System.out.println(coinProportion);
        coinProportionService.add(coinProportion);
        System.out.println("插入数据成功,ID为"+ coinProportion.getId());
        return "redirect:/CoinProportion/showPage";
    }

    //删除
     @RequestMapping("/delete")
     public String delete(String[] id){
         coinProportionService.delete(id);
         return "redirect:/CoinProportion/showPage";
     }


    //编辑
    @RequestMapping(value = "/editing", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String editing(CoinProportion coinProportion){
        coinProportionService.update(coinProportion);
        return "redirect:/CoinProportion/showPage";
    }
}
