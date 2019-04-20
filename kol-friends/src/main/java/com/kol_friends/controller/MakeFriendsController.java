package com.kol_friends.controller;

import com.kol_friends.dto.MakeFriends;
import com.kol_friends.service.MakeFriendsService;
import com.kol_friends.service.MessageService;
import com.kol_friends.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
@RequestMapping("/MakeFriends")
public class MakeFriendsController {
    @Autowired
    MakeFriendsService makeFriendsService;
    @Autowired
    MessageService messageService;

    /**
     * 查询好友列表(返回好友ID列表)
     * @param user_id
     * @return
     */
    @RequestMapping("/getFriendsList")
    @ResponseBody
    public List<MakeFriends> getFriendsList(String user_id){
        List list = makeFriendsService.getFriendsList(user_id);
        return list;
    }

    /***
     * 添加好友
     * @param user_id
     * @param friends_id
     * @return
     * @throws Exception
     */
    @RequestMapping("/addFriends")
    @ResponseBody
    public String addFriends(String user_id,String friends_id) throws Exception {
        //此处还有个做一个判断，添加好友失败不向下执行
        makeFriendsService.addFriends(user_id,friends_id);
        messageService.sendMessage(user_id,friends_id);
        return "新增好友成功";
    }

    /***
     * 增加亲密值
     * @param user_id
     * @param friends_id
     */
    @RequestMapping("/addIntimacy")
    @ResponseBody
    public String addIntimacy(String user_id,String friends_id){
        //异常处理
        makeFriendsService.addIntimacy(user_id,friends_id);
        return "增加亲密度成功";
    }
}
