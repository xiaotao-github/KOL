package com.coin.comment;

import com.coin.dao.GiftOrderDao;
import com.coin.service.GiftOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;


@Controller
public class TimerTaskTest{
    @Autowired
    GiftOrderDao giftOrderDao;
    @Resource
    private JedisPool jedisPool;

    @Scheduled(cron = "0 40 15 * * ?")
    public void test(){
        Jedis jedis = jedisPool.getResource();
        try {
            List list1 = giftOrderDao.weekCharm();
            jedis.set("weekCharm",JsonUtils.objectToJson(list1));
            List list2 = giftOrderDao.weekContribution();
            jedis.set("weekContribution",JsonUtils.objectToJson(list2));
            System.out.println("==========定时器开始工作=========");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

}
