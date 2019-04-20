package com.recharge_cash.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class CoinProportion {
    //序号
    int id;
    //日期
    Date date;
    //X币售价
    String coin_price;
    //X币数量
    int coin_amount;
    //可提现金额
    String cash_money;
    //编辑人
    String editor;

}
