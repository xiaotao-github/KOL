package com.recharge_cash.service;

import com.recharge_cash.comment.JsonUtils;
import com.recharge_cash.dao.CoinRechargeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinRechargeServiceImpl implements CoinRechargeService{
    @Autowired
    CoinRechargeDao coinRechargeDao;
    //金币充值界面（X币兑换比例）
    @Override
    public String coinRecharge() {
        List list = coinRechargeDao.coinRecharge();
        return JsonUtils.objectToJson(list);
    }

    //根据档位id获取充值金额
    @Override
    public int coinRechargeById(String rechargeId) {
        return coinRechargeDao.coinRechargeById(rechargeId);
    }

    //根据充值金额获取X币数量
    @Override
    public int coinRechargeByPrice(int coin_price) {
        return coinRechargeDao.coinRechargeByPrice(coin_price);
    }


}
