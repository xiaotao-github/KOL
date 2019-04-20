package com.kol_friends.service;

import com.kol_friends.dto.Focus;

import java.util.List;

public interface FocusService {
    //获取关注列表
    List getFocusList(String user_id);
    //获取唯一关注的对象
    Focus getOnlyFocus(String user_id, String focus_id);

    void add(Focus focus);
    //添加关注
    void addFocus(String user_id,String focus_id);
    //取消关注
    void cencelFocus(String user_id,String focus_id);


    //提供搜索ID或关键字查询的已关注列表（模糊查询）
    List getFocusListByWord(String user_id, String keyword);

    //添加搜索发现新的用户
    List addFocusListByWord(String keyword);

    //获取关注数量
    String getFocusNum(String user_id);
}
