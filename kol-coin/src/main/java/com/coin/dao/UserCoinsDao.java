package com.coin.dao;


import com.coin.dto.UserCoins;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCoinsDao {
    //更新用户当前余额
    public boolean updateCoins(UserCoins userCoins);
    //   获取当前用户的余额
    public UserCoins getUserCoins(String user_id);
    //新增用户
    boolean addUser(UserCoins userCoins);
}
