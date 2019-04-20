package com.recharge_cash.service;

import java.util.Map;

public interface AliPayOrderService {
    String getAliPayOrderStr(String total_fee,String user_id);
    String notify(Map<String,String> conversionParams);
    Byte checkAlipay(String outTradeNo);
}
