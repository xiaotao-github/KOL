package com.kol_friends.dao;

import com.kol_friends.dto.Fans;
import com.kol_friends.dto.User;

import java.util.List;

public interface FansDao extends Dao<Fans> {

    //获取粉丝列表
    List getFansList(String user_id);
    //获取唯一粉丝的对象
    Fans getOnlyFans(String user_id, String fans_id);

    //提供搜索ID或关键字列表（模糊查询）
    List getFansListByWord(String user_id,String keyword);

    void add(Fans fans);
    //添加粉丝
    void addFans(String user_id,String fans_id);
    //取消粉丝
    void cancelFans(String user_id,String fans_id);

    //获取用户基本信息
    User getUserMessage(String id);
}
