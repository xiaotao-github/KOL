package com.kol_friends.service;


import com.kol_friends.dto.Response;

public interface IntimacyService {
    //获取用户与亲密用户的亲密值
    String getIntimacyValue(String user_id, String intimate_user_id);
    //通过礼物增加亲密值
    Response byGift(String user_id, String intimate_user_id, Integer value);
    //通过聊天时长增加亲密值
    void byChat(String user_id,String intimate_user_id);
}
