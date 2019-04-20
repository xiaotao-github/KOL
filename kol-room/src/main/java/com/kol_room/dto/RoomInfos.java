package com.kol_room.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
//用于模糊搜索房间
public class RoomInfos {
    //用户id
    String uid;
    //房间id
    String roomId;
    //用户头像
    String avatar;
    //房间名称
    String roomName;
    //用户个性签名
    String signature;
}
