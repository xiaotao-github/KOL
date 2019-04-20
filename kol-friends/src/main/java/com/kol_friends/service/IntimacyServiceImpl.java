package com.kol_friends.service;

import com.kol_friends.dto.Response;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntimacyServiceImpl implements IntimacyService {
    @Resource
    JedisPool jedispool;
    // @Resource(name = "giftOrderServiceImpl")
    // GiftOrderService giftOrderService;

    private static final String intimacy = String.valueOf(0);
    private static final Integer intimacy1 = 1;

    private static final Integer intimacy2 = 5;


    //获取亲密度信息
    public String getIntimacyValue(String user_id, String intimate_user_id){
        Jedis jedis=jedispool.getResource();
        //此方法是为了让没有亲密度的创建联系
        try {
            this.isTrue(user_id, intimate_user_id);
            // if (isTrue){
                List list = jedis.hmget(user_id,intimate_user_id);
                System.out.println(list);
                String intimacyValue = (String) list.get(0);
                return intimacyValue;
            // }
            // else {
            //     return 0;
            // }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }

    //判断两个用户之间是否建立亲密度,没有就创建亲密度为0
    public Boolean isTrue(String user_id,String intimate_user_id){
        Jedis jedis=jedispool.getResource();
        try {
            Boolean isTrue = jedis.hexists(user_id, intimate_user_id);
            if (isTrue){
                return true;
            }else {
                Map<String, String> map = new HashMap();
                Map<String, String> map2 = new HashMap();
                map.put(intimate_user_id, intimacy);
                map2.put(user_id, intimacy);
                jedis.hmset(user_id, map);
                jedis.hmset(intimate_user_id, map2);
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }

    //赠送礼物方式增加亲密值
    public Response byGift(String user_id, String intimate_user_id, Integer value){
        //调用礼物模块，获取礼物价值
        // int value = 50;
        Response response;
        Jedis jedis=jedispool.getResource();
        //判断user_id中是否已存在intimate_user_id这个用户
        //两个用户需要增加亲密度
        try {
            this.isTrue(user_id, intimate_user_id);
            //礼物价值小于100,增加1点亲密度
            if (value>=1000){
                jedis.hincrBy(user_id,intimate_user_id,intimacy1);
                jedis.hincrBy(intimate_user_id,user_id,intimacy1);
            }
            //礼物价值大于100,增加5点亲密度
            /*else{
                jedis.hincrBy(user_id,intimate_user_id,intimacy2);
                jedis.hincrBy(intimate_user_id,user_id,intimacy2);
            }*/
            response = new Response("赠送礼物，亲密值增加",null,200);
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试",null,400);
        }finally {
            jedis.close();
        }
        return response;
    }

    //聊天时长(分为房间内聊天与私聊)
    public void byChat(String user_id,String intimate_user_id){
        // Jedis jedis=jedispool.getResource();
        // //判断user_id中是否已存在intimate_user_id这个用户
        // //两个用户需要增加亲密度
        // this.isTrue(user_id, intimate_user_id);
        //
        // //调用房间模块，获取聊天时长
        // int time= 30;
        // int time2 = 30;
        // //房间内与私聊需区分
        // //如果聊天时长每大于30min，则增加一点亲密度
        // if (time>=30||time2>=30){
        //     jedis.hincrBy(user_id,intimate_user_id,intimacy1);
        //     jedis.hincrBy(intimate_user_id,user_id,intimacy1);
        // }
        //
        // //此处少了判断房间内与私聊是否在同一房间
        // return;
    }

    //减少亲密度
}
