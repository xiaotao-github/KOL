package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class GiftList implements Serializable {
    String uid;
    String name;
    String avatar;
    String gift_id;
    String gift_img;
    String gift_name;
    String gift_price;
    int count;
    Date time;
}
