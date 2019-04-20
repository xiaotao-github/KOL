package com.recharge_cash.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoinRechargeDao {
    //金币充值界面列表（X币兑换比例）
    public List coinRecharge();
    //根据档位id获取充值金额
    public int coinRechargeById(String rechargeId);
    //根据充值金额获取X币数量
    public int coinRechargeByPrice(int coin_price);
}
