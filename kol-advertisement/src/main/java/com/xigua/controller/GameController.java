package com.xigua.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xigua.dto.Game;
import com.xigua.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/Game")
public class GameController {
    @Autowired
    GameService gameService;

    @RequestMapping("")
    public String test(){
        return "/game";
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
        List list= gameService.findByProp(map);
        JSONObject obj=new JSONObject();
        //前台通过key值获得对应的value值
        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",list.size());
        obj.put("data",list);
        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        return jsonObject;
        // return "/user";
    }


    //添加
//    @RequestMapping(value = "/doInsert",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
//    @ResponseBody
////    @DateTimeFormat(pattern="yyyy-MM-dd")
//    public String doInsert(User user) throws Exception{
//        System.out.println(user);
//        userService.add(user);
//        System.out.println("插入数据成功,ID为"+user.getId());
//        // return "redirect:/User/findByProp";
//        Map map = new HashMap();
//        List list=userService.findByProp(map);
//        JSONObject obj=new JSONObject();
//        //前台通过key值获得对应的value值
//        obj.put("code", 0);
//        obj.put("msg", "");
//        obj.put("count",list.size());
//        obj.put("data",list);
//        //json换回的数据带时间格式 处理办法.toJSONStringWithDateFormat
//        String jsonObject = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
//        return jsonObject;
////        return "插入成功";
//    }




    //新增数据
    // @RequestMapping(value = "/doInsert",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    // public String doInsert(Game game,@RequestParam("file") MultipartFile file) throws Exception {
    //     System.out.println(game);
    //     //默认上线状态
    //     game.setState(1);
    //     gameService.add(game);
    //     System.out.println("插入数据成功,ID为"+ game.getId());
    //     return "/game";
    // }
    @RequestMapping(value = "/doInsert",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String doInsert(Game game,MultipartFile file){
        Map<String,String> result = new HashMap<>();
        if(file.isEmpty()){
            result.put("code","1");
            result.put("msg","文件为空");
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName +"-->" +size);

        String path = "D:/kol-photo/upload";
        File dest = new File(path+"/"+fileName);
        if (!dest.getParentFile().exists()){//判断文件父目录是否存在
            //不存在则创建一个目录
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);//保存文件
            result.put("code","0");
            result.put("msg","上传成功");
            result.put("url",dest.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code","1");
            result.put("msg","上传失败");
        }catch (IllegalStateException e) {
            e.printStackTrace();
            result.put("code","1");
            result.put("msg","上传失败");
        }
        game.setGame_image(fileName);
        System.out.println(game);
        gameService.add(game);
        System.out.println("插入数据成功,ID为"+ game.getId());
        return "/game";
    }

    //上传文件
    // @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    // @ResponseBody
    // public Map<String,String> upload(@RequestParam("file")MultipartFile file){
    //     Map<String,String> result = new HashMap<>();
    //     if(file.isEmpty()){
    //         result.put("code","1");
    //         result.put("msg","文件为空");
    //     }
    //     String fileName = file.getOriginalFilename();
    //     int size = (int) file.getSize();
    //     System.out.println(fileName +"-->" +size);
    //
    //     String path = "D:/kol-photo/upload";
    //     File dest = new File(path+"/"+fileName);
    //     if (!dest.getParentFile().exists()){//判断文件父目录是否存在
    //         //不存在则创建一个目录
    //         dest.getParentFile().mkdir();
    //     }
    //     try {
    //         file.transferTo(dest);//保存文件
    //         result.put("code","0");
    //         result.put("msg","上传成功");
    //         result.put("url",dest.getPath());
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         result.put("code","1");
    //         result.put("msg","上传失败");
    //     }catch (IllegalStateException e) {
    //         e.printStackTrace();
    //         result.put("code","1");
    //         result.put("msg","上传失败");
    //     }
    //     return result;
    // }

    //删除
     @RequestMapping("/delete")
     @ResponseBody
     public String delete(String[] id){
         gameService.delete(id);
         return "ok";
     }

    //更新
    @RequestMapping(value = "/update", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String update(@RequestParam String id){
        Game game = gameService.findOneById(id);
        game.setState(4);
        gameService.update(game);
        return "/game";
    }

    //上线
    @RequestMapping(value = "/online", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String online(@RequestParam String id){
        Game game = gameService.findOneById(id);
        game.setState(1);
        gameService.update(game);
        return "/game";
    }

    //下线
    @RequestMapping(value = "/offline", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String offline(@RequestParam String id){
        Game game = gameService.findOneById(id);
        game.setState(2);
        gameService.update(game);
        return "/game";
    }

    //编辑
    @RequestMapping(value = "/editing", method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    public String editing(Game game){
        gameService.update(game);
        return "/game";
    }

    //根据查询条件查询
    @RequestMapping(value ="/findByWords",method = RequestMethod.POST,produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String findByWords(
                              @RequestParam(required=false) String id,
                              @RequestParam(required=false) String game_name,
                              @RequestParam(required=false) String game_cp,
                              @RequestParam(required=false) Integer state
                              ){
        Map map=new HashMap();
        if (id==""){
            id=null;
        }else{
            map.put("id",id);
        }
        if (game_name=="") {
            game_name=null;
        }else {
            map.put("game_name",game_name);
        }
        if (game_cp=="") {
            game_cp=null;
        }else {
            map.put("game_cp",game_cp);
        }
        if (state!=null) {
            map.put("state",state);
        }
        List list= gameService.findByProp(map);
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
