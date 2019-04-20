package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Component
@Data
public class Gift implements Serializable {
    //礼物ID
    String id;
    //礼物名称
    String gift_name;
   //礼物类别
    int isHit;
   //平台币类别
   int coin_type;
    //价值
    int value;
    //礼物图片
    String image;
   //git图片
    String gif_image;
    //状态
    int state;
    //房间内开了计数器的礼物价值（含正负）
    int gift_value;
    //魅力值
    int charm_value;
    //可选择数量
    String amount;

    String url;

    MultipartFile file;
}
