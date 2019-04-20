package com.coin.service;


import com.coin.comment.JsonUtils;
import com.coin.dao.GiftOrderDao;
import com.coin.dao.RankingListDao;
import com.coin.dao.UserCoinsDao;
import com.coin.dto.Gift;
import com.coin.dto.GiftOrder;
import com.coin.dto.RankingList;
import com.coin.dto.UserCoins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftOrderServiceImpl implements GiftOrderService{
    @Autowired
    GiftService giftService;
    @Autowired
    UserCoinsService userCoinsService;
    @Autowired
    GiftOrderDao giftOrderDao;
    @Autowired
    UserCoinsDao userCoinsDao;
    @Resource
    JedisPool jedisPool;
    @Autowired
    private RankingListDao rankingListDao;

//    本周魅力榜
    public List weekCharm() {
        return giftOrderDao.weekCharm();
    }

//    魅力榜总榜
    public List totalCharm(){
        return giftOrderDao.totalCharm();
    }

    //    本周贡献榜
    public List weekContribution() {
        //{"uid":"46927767","name":"张三","avatar":"http://thirdqq.qlogo.cn/g?b=oidb&k=71W2GKeLC6fh1I3cACkIRQ&s=100","signature":"用户签名","value":19993}
        return giftOrderDao.weekContribution();
    }

//    贡献榜总榜
    public List totalContribution(){
       return giftOrderDao.totalContribution();
    }


    //    用户赠送礼物,需要事务管理，需要研究一下
    @Transactional
//    public String giveGift(String user_id,String[] receiver_id,String gift_id,int num){
    public Boolean giveGift(List list,String gift_id,String giver_id,int totalValue){
        GiftOrder giftOrder;

//      获取当前用户的余额
        UserCoins userCoins=userCoinsService.getUserCoins(giver_id);
        int coins=userCoins.getUser_coins();
//        判断是否余额是否大于礼物价值
        try {
            if (coins>=totalValue){
                for (int i=0;i<list.size();i++) {
                    giftOrder=(GiftOrder) list.get(i);
                    System.out.println(i+"============"+giftOrder.toString());
                    //新增礼物记录
                    insertOrder(giftOrder);
                    //新增魅力排行榜记录
                    Map<String,Object> rankingList = rankingListDao.selectOneByUid(giftOrder.getAccept_id());
                    if(rankingList!=null){
                        System.out.println("update魅力排行榜记录");
                        rankingList.put("totalCharm",(int)rankingList.get("totalCharm")+giftOrder.getTotalCharmValue());
                        rankingList.put("weekCharm",(int)rankingList.get("weekCharm")+giftOrder.getTotalCharmValue());
                        rankingList.put("uid",rankingList.get("uid"));
                        rankingListDao.update(rankingList);
                    }else{
                        System.out.println("新增魅力排行榜记录");
                        rankingList = new HashMap<>();
                        rankingList.put("totalCharm",giftOrder.getTotalCharmValue());
                        rankingList.put("weekCharm",giftOrder.getTotalCharmValue());
                        rankingList.put("uid",giftOrder.getAccept_id());
                        rankingListDao.insert(rankingList);
                    }
                    //新增贡献排行榜记录
                    Map<String,Object> rankingList2 = rankingListDao.selectOneByUid(giver_id);
                    if(rankingList2!=null){
                        System.out.println("update贡献排行榜记录");
                        rankingList2.put("weekContribution",(int)rankingList2.get("weekContribution")+giftOrder.getTotalValue());
                        rankingList2.put("totalContribution",(int)rankingList2.get("totalContribution")+giftOrder.getTotalValue());
                        rankingList2.put("uid",rankingList2.get("uid"));
                        rankingListDao.update(rankingList2);
                    }else{
                        System.out.println("新增贡献排行榜记录");
                        rankingList2 = new HashMap<>();
                        rankingList2.put("totalContribution",giftOrder.getTotalValue());
                        rankingList2.put("weekContribution",giftOrder.getTotalValue());
                        rankingList2.put("uid",giver_id);
                        rankingListDao.insert(rankingList2);
                    }
                    /**************根据礼物订单id查询赠送礼物价值总数************/

                    giftOrder.getId();
                    //减少赠送者余额
                    reduceCoins(giver_id, totalValue);
                    // //增加赠送者余额
                    // addCoins(giftOrder.getAccept_id(), totalValue);
                }
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return false;
    }


//    赠送礼物的记录
    public void insertOrder(GiftOrder giftOrder){
        giftOrderDao.insertOrder(giftOrder);
    }
//    根据id来查询礼物信息
    public Gift findGiftById(String id){
        return giftOrderDao.findGiftById(id);
    }
//    减少赠送者的余额
    public void reduceCoins(String user_id,int coins){
        UserCoins userCoins=userCoinsDao.getUserCoins(user_id);
        //获取当前用户的旧余额
        int old_coins=userCoins.getUser_coins();
        int new_coins=old_coins-coins;
        userCoins.setUser_coins(new_coins);
        userCoinsDao.updateCoins(userCoins);
    }
//    增加接受者的余额
    public void addCoins(String user_id,int coins){
        UserCoins userCoins=userCoinsDao.getUserCoins(user_id);
        int old_coins=userCoins.getUser_coins();
        int new_coins=old_coins+coins;
        userCoins.setUser_coins(new_coins);
        userCoinsDao.updateCoins(userCoins);
    }
//    查询用户收到过的礼物
    public List findAcceptGiftByUser(String user_id){
        List list=giftOrderDao.findAcceptGiftByUser(user_id);
        return list;
    }

    //    查询用户送出的礼物
    public List findGiveGiftByUser(String user_id){
        List list=giftOrderDao.findGiveGiftByUser(user_id);
        return list;
    }

    //查询用户收到过的礼物(用于用户主页展示)
    public List findGiveGiftByUserToUser(String user_id){
        List list=giftOrderDao.findGiveGiftByUserToUser(user_id);
        return list;
    }

//    用户的礼物界面，可以看出用户送出了什么礼物和收到了什么礼物
    public String findGiftListByUser(String user_id){
//        查询收到的礼物
        List receivedGift=giftOrderDao.findAcceptGiftByUser(user_id);
//        查询送出的礼物
        List giveGift=giftOrderDao.findGiveGiftByUser(user_id);
        Map map=new HashMap();
        map.put("receivedGift",receivedGift);
        map.put("giveGift",giveGift);
        return JsonUtils.objectToJson(map);
    }

    //金币充值界面（X币兑换比例）
    @Override
    public String coinRecharge() {
        List list = giftOrderDao.coinRecharge();
        return JsonUtils.objectToJson(list);
    }

    //根据档位id获取充值金额
    @Override
    public Double coinRechargeById(String rechargeId) {
        return giftOrderDao.coinRechargeById(rechargeId);
    }

    //提供礼物值接口
    @Override
    public String getTotalCharmValue(String user_id){
        String totalCharmValue = giftOrderDao.getTotalCharmValue(user_id);
        return totalCharmValue;
    }
}
