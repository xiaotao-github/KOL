// import com.alibaba.fastjson.JSONObject;
// import com.kol_room.comment.HttpRequest;
// import com.kol_room.dto.UserDetail;
// import com.kol_room.service.UserRoomService;
// import org.apache.commons.codec.binary.Base64;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
// import redis.clients.jedis.JedisPool;
//
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration("classpath*:config/applicationContext-*.xml")
// public class test {
//     @Autowired
//     UserRoomService us;
//     @Autowired
//     JedisPool jedisPool;
//
//     @Test
//     public void test1() {
//         // UserDetail up = us.up_phone("1", 1, "1");
//         // System.out.println(up.toString());
//     }
//     @Test
//     public void test2(){
//         us.addRoomPeople("9999","9");
//     }
//     @Test
//     public void test3(){
//         us.showRoomPeople("9999");
//     }
//     @Test
//     public void test4(){
//         us.deleteRoomPeople("9999","9");
//     }
//     @Test
//     public void test5() throws ClassNotFoundException {
//         us.show_microphone("1");
//     }
//     @Test
//     public void test6(){
//         // us.down_phone("1",1);
//     }
//     @Test
//     public void test7(){
//         String result=HttpRequest.sendGet("https://api.agora.io/dev/v1","");
//         System.out.println(result);
//     }
//     @Test
//     public void test8(){
//         JSONObject jsonObject=new JSONObject();
//
//         String name="c";
// //        jsonObject.put("id",);
// //        jsonObject.put("name",name);
// //        HttpRequest.sendGet()
//     }
//     @Test
// public void test9(){
//         String agora="e405903be3a647048e05ad69420d99e2:bd373c9e4e244cc9aa105a19fc80fe6e";
//         String base64Credentials = new String(Base64.encodeBase64(agora.getBytes()));
//     System.out.println(base64Credentials);
// }
// }
