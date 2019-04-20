package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class UserGift implements Serializable {
    String gift_id;
    String gift_img;
    String gift_name;
    String gift_price;
    String count;
}
