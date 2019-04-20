package com.kol_room.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class Room {
    //房间id，也是房主id
    String id;
    //房间名
    String room_name;
    //房间话题
    String room_topic;
//  房间标签
    String room_label;
//    房主头像
//     String user_image;
    //房主昵称
    String username;
    //房间人数,这个应该由缓存来看
    Long room_number;
    //发布时间
    Date time;
    //创建时间
    Date create_time;
    //状态
    int state;
//    房间权重
    int room_position;
//    房间背景图
    String room_image;
//    房间公告
    String room_notice;
//    房间限制
    int room_limit;
//    房间类型
    int room_type;

}
