package com.recharge_cash.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Data
public class Recharge {
    //日期
    Date date;
    //订单号
    String order_number;
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

}
