package com.recharge_cash.service;

public interface CoinRechargeService {
    //金币充值界面列表（X币兑换比例）
    public String coinRecharge();
    //根据档位id获取充值金额
    public int coinRechargeById(String rechargeId);
    //根据充值金额获取X币数量
    public int coinRechargeByPrice(int coin_price);
}
