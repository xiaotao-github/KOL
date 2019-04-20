import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/applicationContext-*.xml")
public class test {
    @Autowired
    JedisPool jedisPool;

    @Test
    public void testJedis(){
        Jedis jedis = jedisPool.getResource();
        Map<String,String> map = new HashMap();
        String room_id = "888";
        String user_id = "545454";
        int duration = 20;
        String key = map.put(user_id, String.valueOf(duration));
        if (duration==-1){
            jedis.hmset("roomAdmin_"+room_id,map);
        }else {
            jedis.expire(key,duration);
            jedis.hmset("roomAdmin_"+room_id,map);
        }
        // jedis.expire("roomAdmin_"+room_id,duration);
        // jedis.get("roomAdmin_"+room_id);
        // System.out.println(jedis.get("roomAdmin_"+room_id));
        // jedis.lpush("roomAdmin_"+room_id,user_id);
        jedis.close();
    }

    @Test
    public void test2(){
        Jedis jedis = jedisPool.getResource();
        jedis.hset("me","name","tom");
        System.out.println(jedis.hget("me","name"));
        jedis.expire("me",10);
    }

    @Test
    public void test3(){
        int i=(int)(Math.random()*3);
        System.out.println(i);
    }
}
