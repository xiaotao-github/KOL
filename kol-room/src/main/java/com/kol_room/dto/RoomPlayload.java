package com.kol_room.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RoomPlayload {
//    iss jwt签发者
//    String iss;
////    sub: jwt所面向的用户
//    String sub;
////    aud: 接收jwt的一方
//    String aud;
////    exp: jwt的过期时间，这个过期时间必须要大于签发时间
//    String exp;
////    nbf: 定义在什么时间之前，该jwt都是不可用的.
//    String nbf;
////            iat: jwt的签发时间
//    String iat;
//    jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
//    String jti;
    String username;
    String user_id;
    String user_image;
    //    个人签名
    String introduction;
}
