import com.home.dto.Home;
import com.home.service.HomeService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/applicationContext-*.xml")
public class test {
    @Autowired
    HomeService homeService;
    @Test
    public void test1(){
        Home home = homeService.findOneById("dfd");
        home.setContent_title("213131313133");
        homeService.update(home);
    }
    @Test
    public void test2(){
        String newDate = String.valueOf(System.currentTimeMillis() / 1000);
        System.out.println(newDate);
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        System.out.println(newDate + result);
    }
    @Test
    public void test3(){
        String s = "20190411104751";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = sdf.parse(s);
            // SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // String format = sdf1.format(date);
            String date1 = date.toString();
            System.out.println(date1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.US);
            Date d = sdf1.parse(date1);
            System.out.println(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() throws ParseException {
        // DecimalFormat sdf = new DecimalFormat("######0.00");
        // Double s = Double.valueOf(33);
        // String s1 = sdf.format(s);
        // System.out.println(s1);

        // String s = "33.00";
        // double i = Double.valueOf(s);
        // int ss = (int) i;
        // System.out.println(ss);

        String gmt_payment="2019-04-11 15:41:14";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(gmt_payment);
        System.out.println(date);
    }
}
