package com.kol_friends.service;

import com.kol_friends.dto.MakeFriends;

import java.util.List;


public interface MakeFriendsService {
    //返回好友列表ID
    List getFriendsList(String user_id);

    void add(MakeFriends makeFriends);
    //添加好友
    void addFriends(String user_id, String friends_id);
    //增加亲密度
    void addIntimacy(String user_id, String friends_id);

    void update(MakeFriends makeFriends);

}
