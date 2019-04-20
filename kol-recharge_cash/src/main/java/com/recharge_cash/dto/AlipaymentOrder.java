package com.recharge_cash.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class AlipaymentOrder {
//    String clubOrderId;//商家订单主键
//    商户订单号
    String outTradeNo;
//    交易状态
    Byte tradeStatus;
//    订单金额
    Double totalAmount;
//    实收金额
    Double receiptAmount;
//    开票金额
    Double invoiceAmount;
//    付款金额
    Double buyerPayAmount;
//    总退款金额
    Double refundFee;
//    异步回调的地址
    String notifyUrl;
//通知时间:yyyy-MM-dd HH:mm:ss
    Date notifyTime;
//    交易创建时间:yyyy-MM-dd HH:mm:ss
    Date gmtCreate;
//    交易付款时间
    Date gmtPayment;
//    交易退款时间
    Date gmtRefund;
//    交易结束时间
    Date gmtClose;
//    支付宝的交易号
    String tradeNo;
//    商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
    String outBizNo;
//    买家支付宝账号
    String buyerLogonId;
}
