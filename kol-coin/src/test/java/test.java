
import com.coin.comment.JsonUtils;
import com.coin.dto.Gift;
import com.coin.dto.GiftOrder;
import com.coin.service.GiftOrderServiceImpl;
import com.coin.service.GiftService;
import com.coin.service.UserCoinsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/applicationContext-*.xml")
public class test {
    @Autowired
    UserCoinsService userCoinsService;
    @Autowired
    GiftOrderServiceImpl giftOrderService;
    @Autowired
    GiftService giftService;
    @Test
    public void test1(){
      List list= giftOrderService.weekCharm();
       String s= JsonUtils.objectToJson(list);
        System.out.println(s);
    }
    @Test
    public void test2(){
        List list= giftOrderService.totalCharm();
        String s=JsonUtils.objectToJson(list);
        System.out.println(s);
    }
    @Test
    public void test3(){
        List list= giftOrderService.weekContribution();
        String s=JsonUtils.objectToJson(list);
        System.out.println(s);
    }
    @Test
    public void test4(){
        List list= giftOrderService.totalContribution();
        String s=JsonUtils.objectToJson(list);
        System.out.println(s);
    }
    @Test
    public void test5(){
        GiftOrder giftOrder=new GiftOrder();
        giftOrder.setGiver_id("2");
        giftOrder.setAccept_id("3");
        giftOrder.setGift_id("4");
        giftOrderService.insertOrder(giftOrder);
    }
    @Test
    public void test6(){
        giftOrderService.addCoins("2",100);
    }
    @Test
    public void test7(){
        Gift gift = giftOrderService.findGiftById("1");
        System.out.println(gift.toString());
    }
//    @Test
//    public void giftList(){
//        List list= giftOrderService.findGiftByUser("1");
//        System.out.println(list.toString());
//    }

}
