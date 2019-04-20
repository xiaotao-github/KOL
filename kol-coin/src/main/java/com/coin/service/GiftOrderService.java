package com.coin.service;


import com.coin.dto.Gift;
import com.coin.dto.GiftOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiftOrderService {
    //    本周魅力榜,本周收到礼物价值榜单
    List weekCharm();
    //    魅力榜总榜
    List totalCharm();
    //    本周贡献榜，本周送出礼物价值榜单
    List weekContribution();
    //    贡献榜总榜
    List totalContribution();
    //    用户赠送礼物,需要事务管理，需要研究一下
    Boolean giveGift(List list, String gift_id, String giver_id, int totalValue);
    //    根据id来查询礼物信息
    Gift findGiftById(String id);
    //    减少赠送者的余额
    void reduceCoins(String user_id, int coins);
    //    增加接受者的余额
    void addCoins(String user_id, int coins);
    //    查询用户收到过的礼物
    List findAcceptGiftByUser(String user_id);
    //    查询用户送出的礼物
    List findGiveGiftByUser(String user_id);

    //用户主页详情的送出礼物列表
    List findGiveGiftByUserToUser(String user_id);
    //    赠送礼物的记录
    void insertOrder(GiftOrder giftOrder);
    //    用户的礼物界面，可以看出用户送出了什么礼物和收到了什么礼物
    String findGiftListByUser(String user_id);

    //金币充值界面列表（X币兑换比例）
    String coinRecharge();
    //根据档位id获取充值金额
    Double coinRechargeById(String rechargeId);

    //提供礼物值接口
    String getTotalCharmValue(String user_id);

    //根据赠送礼物id查询订单信息
    // String findGiftById(String id);
}
