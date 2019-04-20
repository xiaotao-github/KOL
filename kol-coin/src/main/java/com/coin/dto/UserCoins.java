package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class UserCoins implements Serializable {
    //用户id
    String user_id;
    //平台币余额
    int user_coins;
}
