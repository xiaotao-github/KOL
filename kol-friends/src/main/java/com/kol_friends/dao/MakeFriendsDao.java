package com.kol_friends.dao;

import com.kol_friends.dto.MakeFriends;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MakeFriendsDao extends Dao<MakeFriends> {
    //根据用户id查询好友列表（返回好友ID列表）
    List getFriendsList(String user_id);

    //获取唯一对应的好友
    MakeFriends getOnlyFriends(String user_id,String friends_id);

    void add(MakeFriends makeFriends);
    //添加好友
    void addFriends(String user_id,String friends_id);

    //增加亲密值
    void addIntimacy(String user_id,String friends_id,int intimacyValue);

    //更新操作
    void update(MakeFriends makeFriends);


    //删除好友
}
