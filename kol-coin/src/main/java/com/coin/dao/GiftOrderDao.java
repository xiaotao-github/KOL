package com.coin.dao;


import com.coin.dto.Gift;
import com.coin.dto.GiftOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GiftOrderDao {
    public void insertOrder(GiftOrder giftOrder);
    public List weekCharm();
    public List totalCharm();
    public List weekContribution();
    public List totalContribution();
    public Gift findGiftById(String id);
    public List findAcceptGiftByUser(String user_id);
    public List findGiveGiftByUser(String user_id);
    public List findGiveGiftByUserToUser(String user_id);

    //金币充值界面列表（X币兑换比例）
    public List coinRecharge();
    //根据档位id获取充值金额
    public Double coinRechargeById(String rechargeId);
    public String getTotalCharmValue(String user_id);


}
