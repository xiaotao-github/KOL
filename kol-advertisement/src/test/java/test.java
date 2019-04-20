import com.xigua.dao.GameDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/applicationContext-*.xml")
public class test {
    @Autowired
    GameDao gameDao;
    @Test
    public void testfingbyprop(){
        Map map=new HashMap();
        List list =gameDao.findByProp(map);
        System.out.println(list);
    }
}
