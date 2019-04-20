package com.kol_friends.service;

import com.kol_friends.dto.Message;

import java.util.List;
import java.util.Map;

public interface MessageBackstageService {
    List findByProp(Map map);
    Message findOneById(int id);
    boolean add(Message message);
    boolean update(Message message);
    boolean delete(int[] id);

    //消息列表
    List getMessageList();
}
