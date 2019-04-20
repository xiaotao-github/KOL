import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/applicationContext-*.xml")
public class test {
    @Autowired

    @Test
    public void test1(){
        Random ra =new Random();
        System.out.println(ra.nextInt(100000));
    }
    @Test
    public void test2(){
        System.out.println((int)(Math.random()*100000));
    }
}
