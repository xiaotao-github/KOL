package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GiftListToRoom {
    String gift_id;
    String gift_img;
    String gift_name;
    //礼物价值
    String gift_price;
    //礼物值
    String gift_value;
    int isHit;
    String amount;
    String url;
}
