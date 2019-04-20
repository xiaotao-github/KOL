package com.recharge_cash.dao;

import com.recharge_cash.dto.AlipaymentOrder;
import com.recharge_cash.dto.RechargeOrder;
import com.recharge_cash.dto.WXPaymentOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RechargeOrderDao {
    //    新的商户支付宝订单
    public boolean createRechargeOrder(RechargeOrder rechargeOrder);
    //根据订单号来查订单
    public RechargeOrder selectBytransactionId(String transaction_id);
    //    更新表记录
    public Boolean updateByPrimaryKey(RechargeOrder rechargeOrder);

}
