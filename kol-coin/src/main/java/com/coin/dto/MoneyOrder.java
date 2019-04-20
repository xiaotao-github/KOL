package com.coin.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MoneyOrder {
    //订单号
    String outTradeNo;
//    商品名称
    String goodName;
//    商品数量
    int goodsNum;
//    商品单价
    Double oneGoodMoney;
//    商品总价
    Double totalGoodsMoney;
//    购买人id
    String buyerID;
//    商品id
    String goodsID;

}
