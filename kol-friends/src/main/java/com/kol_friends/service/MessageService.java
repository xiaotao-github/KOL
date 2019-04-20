package com.kol_friends.service;


public interface MessageService {
    //发送已关注用户开房间后的系统通知
    void sendMessage(String user_id, String friends_id) throws Exception;
    //发送全体消息
    void sendMessageToAll() throws Exception;

}
