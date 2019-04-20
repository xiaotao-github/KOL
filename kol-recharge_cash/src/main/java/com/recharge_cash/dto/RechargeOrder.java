package com.recharge_cash.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class RechargeOrder {
    //日期
    Date date;
    //订单号
    String order_number;
    //第三方分配订单号
    String transaction_id;
    //用户ID
    String id;
    //用户姓名
    String username;
    //金额
    int money;
    //支付方式
    int payment;
    //账户余额
    int account_balance;
    //操作行为
    int operant_hehavior;
    //状态
    byte state;
    //交易结束时间
    Date date_end;
    //交易类型
    String trade_type;
    //交易银行
    String bank_type;
}
