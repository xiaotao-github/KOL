package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class GiftOrder implements Serializable {
    String id;
//    赠送者id
    String giver_id;
//    接受者id
    String accept_id;
//    礼物id
    String gift_id;
//    礼物单价
    int unitPrice;
//    赠送时间
    String give_time;
//    赠送的数量
    int gift_num;
//赠送的总体价值
    int totalValue;
//    礼物的魅力值
    int charmValue;
//    赠送的总体魅力值
    int totalCharmValue;
//    麦位信息
    int position;
//    房间id
    String room_id;

}
