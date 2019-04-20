package com.recharge_cash.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class WXPaymentOrder {
    //预支付交易会话标识，该值有效期为2小时
    String prepay_id;
    //商户订单号
    String out_trade_no;
    //微信生成订单号
    String transaction_id;

    //交易类型
    String trade_type;
    //签名类型
    String sign_type;

    //交易起始时间(预订单生成时间)
    Date time_start;
    // //交易结束时间（预订单失效时间）
    // Date time_expire;
    //支付完成时间(可由订单完成时返回的数据获取,注意微信返回的是时间戳格式20141030133525)
    Date time_end;

    //通知地址
    String notify_url;
    //开发票入口开放标识
    String receipt;

    //总金额
    int total_fee;
    //交易状态
    Byte trade_state;
    //付款银行
    String bank_type;
}
