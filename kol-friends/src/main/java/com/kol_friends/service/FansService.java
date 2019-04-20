package com.kol_friends.service;

import com.kol_friends.dto.Fans;

import java.util.List;


public interface FansService {
    //获取粉丝列表
    List getFansList(String user_id);
    //获取唯一粉丝的对象
    Fans getOnlyFans(String user_id, String fans_id);

    //提供搜索ID或关键字列表（模糊查询）
    List getFansListByWord(String user_id, String keyword);

    void add(Fans fans);

    void addFans(String user_id,String fans_id);

    void cancelFans(String user_id,String fans_id);

    //获取粉丝数量
    String getFansNum(String user_id);

}
