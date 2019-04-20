package com.kol_friends.dao;

import com.kol_friends.dto.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageBackstageDao extends Dao<Message>{
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    Message findOneById(int id);
    //   添加
    boolean add(Message message);
    //   根据ID删除
    boolean delete(int[] id);
    //    根据ID修改
    boolean update(Message message);
    //消息列表
    List getMessageList();
    //获取用户id列表
    List getUserIdList();
}
