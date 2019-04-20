package com.coin.service;

import com.coin.dao.UserCoinsDao;
import com.coin.dto.UserCoins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCoinsServiceImpl implements UserCoinsService {

    @Autowired
    UserCoinsDao userCoinsDao;

    //  微信用户充值
    @Override
    public String WXreplenishCoins() {
        return null;
    }

    //   支付宝用户充值
    @Override
    public String ZFBreplenishCoins() {
        return null;
    }


    //新增用户
    public boolean addUser(UserCoins userCoins){return userCoinsDao.addUser(userCoins);}

    //   获取当前用户的余额
    @Override
    public UserCoins getUserCoins(String user_id) {
        return userCoinsDao.getUserCoins(user_id);
    }

    //更新用户当前余额
    @Override
    public boolean updateCoins(UserCoins userCoins) {
        return userCoinsDao.updateCoins(userCoins);
    }
}
