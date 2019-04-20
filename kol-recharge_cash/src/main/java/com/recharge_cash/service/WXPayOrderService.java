package com.recharge_cash.service;

import java.util.Map;

public interface WXPayOrderService {
    Map<String,String> getUnifiedOrder(int total_fee, String user_id) throws Exception;
    String payBack(String notifyData) throws Exception;
    Map<String, String> orderInquiry(String transaction_id) throws Exception;
    String paymentState(String transaction_id, String trade_type) throws Exception;
}
