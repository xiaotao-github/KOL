package com.kol_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirstPageService {
    @Autowired
    UserService userService;
//    首页进行查询游戏
    public List search(String keyWord){
//        根据关键字模糊查询,查询结果按照这个顺序
//a.	游戏/视频与搜索词命中度越高，排名越前
//b.	游戏下载量，视频点赞量，排名越前
//c.	点击搜索结果页的视频播放，跳转到小视频的播放界面
//d.	错误页：根据关键字无法搜索到结果，显示搜索结果为空
        Map map=new HashMap<String,String>();
        map.put("keyWord",keyWord);
        List list=userService.findByProp(map);
        if (list==null){
            return null;
        }
        return list;
    }
//    六条顶部广告的接口
    public List topAdvertisement(){
        List list=userService.topAdvertisement();
        return list;
    }
//    首页的创建房间
    public void createRoom(String id){
//        先根据id来进行查询用户有没有房间
//        等待房间模块可以调用
    }
}
