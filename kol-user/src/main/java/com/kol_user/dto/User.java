package com.kol_user.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class User implements Serializable {
    //用户ID
    String id;
    //用户昵称
    String username;
    String password;
    //性别
    int sex;
    String introduction;
    String telephone;

//    用户头像
    String user_image;
//    注册时间
    Date register_time;
//    用户位置
    String position;
    //在线时长
    Date login_time;
//    登录类型
    int identity_type;
//    登录类型标识
    String identity_id;
//    删除用户
    int user_status;
//    用户状态
    int state;

    //    用户类型
    int user_type;
    //    身高
    String height;
    //    年龄
    String age;
    //   兴趣爱好
    String hobby;
}
