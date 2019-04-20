package com.kol_friends.service;

import com.kol_friends.dao.MessageBackstageDao;
import com.kol_friends.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageBackstageServiceImpl implements MessageBackstageService{
    @Autowired
    MessageBackstageDao messageBackstageDao;
    @Autowired
    MessageService messageService;

    public List findByProp(Map map){
        return  messageBackstageDao.findByProp(map);
    }
    public Message findOneById(int id){
        return  messageBackstageDao.findOneById(id);
    }
    public boolean add(Message message){
        //添加消息推送，立即发送全体消息
        try {
            boolean b = messageBackstageDao.add(message);
            if (b){
                messageService.sendMessageToAll();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean update(Message message){
        return messageBackstageDao.update(message);
    }
    public boolean delete(int[] id){
        return messageBackstageDao.delete(id);
    }
    public List getMessageList(){
        return  messageBackstageDao.getMessageList();
    }
}
