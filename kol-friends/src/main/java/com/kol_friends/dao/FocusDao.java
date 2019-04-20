package com.kol_friends.dao;

import com.kol_friends.dto.Focus;
import com.kol_friends.dto.MakeFriends;
import com.kol_friends.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FocusDao extends Dao<Focus> {
    //获取关注列表
    List getFocusList(String user_id);
    //获取唯一关注的对象
    Focus getOnlyFocus(String user_id, String focus_id);

    void add(Focus focus);
    //添加关注
    void addFocus(String user_id,String focus_id);
    //取消关注
    void cencelFocus(String user_id,String focus_id);

    //提供搜索ID或关键字列表（模糊查询）
    List getFocusListByWord(String user_id,String keyword);

    //添加搜索发现新的用户
    List addFocusListByWord(String keyword);

    //获取用户基本信息
    User getUserMessage(String id);
}
