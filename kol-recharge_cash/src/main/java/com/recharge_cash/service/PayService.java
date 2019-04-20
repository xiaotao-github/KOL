package com.recharge_cash.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PayService {
    //充值订单生成
    String getOrderByAliPay(String rechargeId, String user_id);
    Map<String, String> getOrderByWXPay(String rechargeId, String user_id);
    byte orderInquiry(String order_number);
}
