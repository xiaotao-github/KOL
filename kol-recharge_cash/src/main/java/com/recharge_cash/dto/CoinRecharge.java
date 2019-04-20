package com.recharge_cash.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CoinRecharge {
    int rechargeId;
    int coin_price;
    int coin_amount;
}
