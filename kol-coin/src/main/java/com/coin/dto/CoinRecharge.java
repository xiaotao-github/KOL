package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CoinRecharge {
    int rechargeId;
    String coin_price;
    String coin_amount;
}
