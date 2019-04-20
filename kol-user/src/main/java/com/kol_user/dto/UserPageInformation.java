package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class UserPageInformation implements Serializable {
    //id
    String uid;
    //头像
    String avatar;
    //昵称
    String name;
    //性别
    int sex;
    //简介
    String signature;
    //位置
    String position;
    //    身高
    String height;
    //    年龄
    String age;
    //   兴趣爱好
    String hobby;
    //性别修改次数
    String sexTime;
}
