package com.coin.service;


import com.coin.dto.UserCoins;
import org.springframework.stereotype.Service;

@Service
public interface UserCoinsService {
//  微信用户充值
   public String WXreplenishCoins();
//   支付宝用户充值
   public String ZFBreplenishCoins();

   //新增用户
   public boolean addUser(UserCoins userCoins);

//   获取当前用户的余额
    public UserCoins getUserCoins(String user_id);
    //更新用户当前余额
    public boolean updateCoins(UserCoins userCoins);
}
