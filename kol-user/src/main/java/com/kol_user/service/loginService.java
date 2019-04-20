package com.kol_user.service;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


import com.coin.service.UserCoinsService;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.kol_user.comment.IDUtil;


import com.kol_user.comment.JsonUtils;
import com.kol_user.comment.MD5;
import com.kol_user.comment.TokenUtil;
import com.kol_user.dao.UserDao;
import com.kol_user.dto.Response;
import com.kol_user.dto.Room;
import com.kol_user.dto.User;
import com.kol_user.dto.UserCoins;
import io.rong.models.response.TokenResult;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class loginService {
    @Autowired
    JedisPool jedispool;
    @Autowired
    UserService userService;
    @Autowired
    GetTokenService getTokenService;
    @Autowired
    UserDao userDao;
    // @Resource(name = "userCoinsServiceImpl")
    // UserCoinsService userCoinsService;


    //    短信应用SDK AppID
    static int appid=1400182901;
    //    短信应用SDK AppKey
    static String appkey="9df823d4d2ebc49c6742400f8d4fca58";
    //    短信模板ID，需要在短信应用中申请
    static int templateId=271803;
    //    签名
    String smsSign="上海箭塔互娱";
    Random random=new Random();



    public Map giveMessageService(String telephone){
        String rdNumber=(String.valueOf((Math.random()*9+1)*1000)).substring(0,4);
        Map telephone_rdNum_Map=new HashMap();
        //    需要发送短信的手机号码
        String[] phoneNumbers={telephone};
        try {
            String[] params={rdNumber};
            telephone_rdNum_Map.put(telephone,rdNumber);
            System.out.println(rdNumber);
//            System.out.println((boolean) telephone_rdNum_Map.containsKey("13710033462"));
            SmsSingleSender ssender=new SmsSingleSender(appid,appkey);
            SmsSingleSenderResult result=ssender.sendWithParam("86",phoneNumbers[0],templateId,params,smsSign,"","");
            System.out.println(result);
        }catch (JSONException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (HTTPException e) {
            e.printStackTrace();
        }
        return telephone_rdNum_Map;
    }


    //      将发送信息的手机号码和验证码填入redis
    public void setRdNumToRedis(Map<String, String> telephone_rdNum_Map){
//        String telephone=new String();
        Jedis jedis=jedispool.getResource();
        try {
            for (String telephone : telephone_rdNum_Map.keySet()){
                jedis.set(telephone,telephone_rdNum_Map.get(telephone));
                //此处设置redis的过期时间（键值全消失），手机号（验证码）60秒后消失
                jedis.expire(telephone,300);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

//    用户获取验证码
    public void getRdNum(String telephone){
        Map<String,String> telephone_rdNum_Map=new HashMap<String,String>();
        telephone_rdNum_Map=giveMessageService(telephone);
        setRdNumToRedis(telephone_rdNum_Map);
    }


    //使用手机号和验证码登录时获取登录验证码
    public void getUserRdnum(String telephone){
        Map<String,String> telephone_userRdnum_Map=new HashMap<String,String>();
        telephone_userRdnum_Map=giveMessageService(telephone);
        setUserRdnumToRedis(telephone_userRdnum_Map);
    }

    //      将发送信息的手机号码和验证码填入redis
    public void setUserRdnumToRedis(Map<String, String> telephone_userRdnum_Map){
        Jedis jedis=jedispool.getResource();
        try {
            for (String telephone : telephone_userRdnum_Map.keySet()){
                System.out.println(telephone);
                System.out.println(telephone_userRdnum_Map.get(telephone));
                jedis.set(telephone,telephone_userRdnum_Map.get(telephone));
                //此处设置redis的过期时间（键值全消失），手机号（验证码）60秒后消失
                jedis.expire(telephone,300);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    //注册，存储手机号和密码MD5加密
    public void register(String telephone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String md5Password= MD5.getEncryptedPwd(password);
        userService.addTelephone(telephone, md5Password);
    }


    //    用户校验验证码
    public boolean isRdNum(String telephone, String userRdnum){
        Jedis jedis=jedispool.getResource();
        try {
            String rdNum=jedis.get(telephone);
            boolean isTrue=rdNum.equals(userRdnum);
            if (isTrue){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }

    //    手机+验证码登录
    public String RdNumLogin(String telephone, String userRdnum) throws Exception {
        Jedis jedis=jedispool.getResource();
        JSONObject res=new JSONObject();
        try {
            String rdNum=jedis.get(telephone);
            if (rdNum!=null && rdNum.length()>0){
                boolean isTrue=rdNum.equals(userRdnum);
                if (isTrue){
                    User user = userService.findOneByTelephone(telephone);
                    if (user==null){
                        //没有电话号码的情况
                        userService.addTelephone(telephone,"");
                        User user1 = userService.findOneByTelephone(telephone);
                        user1.setSex(1);
                        user1.setIdentity_type(2);
                        user1.setState(1);
                        user1.setUser_status(1);
                        user1.setUser_type(1);
                        userService.updateImformation(user1);
                        return this.getResponse(user1,0);
                    }
                    user.setIdentity_type(2);
                    return this.getResponse(user,0);
                }else {
                    res.put("status",302);
                    res.put("msg","验证码错误");
                    res.put("data",null);
                    return res.toJSONString();
                }
            }else {
                res.put("status",309);
                res.put("msg","验证码已过期");
                res.put("data",null);
                return res.toJSONString();
            }
        }catch (Exception e){
            e.printStackTrace();
            res.put("status",400);
            res.put("msg","系统错误，请稍后重试");
            res.put("data",null);
            return res.toJSONString();
        }finally {
            jedis.close();
        }
    }


    //    根据号码和密码登录
    public String loginByphoneAndpassword(String telephone, String password) throws Exception {
        JSONObject res=new JSONObject();
        //        查出号码
        try {
            User user=userService.findOneByTelephone(telephone);
            boolean isTrue=MD5.validPassword(password,user.getPassword());
            if (isTrue){
                user.setSex(1);
                user.setIdentity_type(1);
                user.setState(1);
                user.setUser_status(1);
                user.setUser_type(1);
                userService.updateImformation(user);
                return this.getResponse(user,0);
            }else {
                res.put("status",307);
                res.put("msg","用户名密码错误");
                res.put("data",null);
                return res.toJSONString();
            }
        }catch (Exception e){
            e.printStackTrace();
            res.put("status",400);
            res.put("msg","登录失败");
            res.put("data",null);
            return res.toJSONString();
        }
    }

    //    根据第三方登录
    public String loginByOther(String tid, String tnickname, String tavatar,String gender,String type) throws Exception {
//        根据这个id去查询有没有这个人
        User user=userService.findOneByIdentityId(tid);
        if (user==null){
            User user1 = new User();
            //  user为空,则进行添加操作
            user1.setIdentity_id(tid);
            user1.setId(IDUtil.createAppCode());
            user1.setUsername(tnickname);
            user1.setUser_image(tavatar);
            user1.setSex(Integer.parseInt(gender));
            user1.setIdentity_type(Integer.parseInt(type));
            user1.setState(1);
            user1.setUser_status(1);
            user1.setUser_type(1);
            userService.add(user1);
            /************添加用户到user_coin表中*************/
            UserCoins userCoins = new UserCoins();
            userCoins.setUser_id(user1.getId());
            userDao.addUser(userCoins);
            // userCoinsService.addUser(userCoins);
            return this.getResponse(user1,1);
        }else {
//            1、如果不是空，代表这个用户登录过，将昵称和头像添加进数据库
//             user.setUsername(tnickname);
//             user.setUser_image(tavatar);
//             userService.updateImformation(user);
//            2、添加完毕之后将根据id查询然后发送给前端
            user=userService.findOneById(user.getId());
            return this.getResponse(user,1);
        }

        // return null;
    }

    /**
     *
     * @param user
     * @param type 判断是否第三方登录1第三方0非第三方
     * @return
     * @throws Exception
     */
    public String getResponse(User user,int type) throws Exception {
        String token= TokenUtil.getJwtToken(user.getId());
        String user_image = user.getUser_image();
        //非第三方登录
        if(type==0) {
            //获取融云token
            TokenResult rongyuntoken = getTokenService.getToken(user.getId(), user.getUsername(), user_image);
            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> map1 = new HashMap<String, String>();
            map.put("ryToken", rongyuntoken.getToken());
            map.put("token", token);
            /************把token放到redis中************/
            Jedis jedis = jedispool.getResource();
            try {
                map1.put(user.getId(), token);
                jedis.hmset("userToken", map1);
                Room room = userService.findRoomById(user.getId());
                if (room!=null&& !TextUtils.isEmpty(room.getId())){
                    map.put("roomId",room.getId());
                }
                /**********把token放到redis中*****************/
                map.put("uid", user.getId());
                map.put("name", user.getUsername());
                map.put("avatar", user_image);
                map.put("gender", "" + user.getSex());
                Response response = new Response("登录成功", JsonUtils.objectToJson(map), 200);
                return JsonUtils.objectToJson(response);
            }catch (Exception e){
                Response response = new Response("系统错误，请稍后重试", null, 400);
                return JsonUtils.objectToJson(response);
            }finally {
                jedis.close();
            }
        }else {
            //获取融云token
            TokenResult rongyuntoken = getTokenService.getToken(user.getId(), user.getUsername(), user.getUser_image());
            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> map1 = new HashMap<String, String>();
            map.put("ryToken", rongyuntoken.getToken());
            map.put("token", token);
            /************把token放到redis中************/
            Jedis jedis = jedispool.getResource();
            try {
                map1.put(user.getId(), token);
                jedis.hmset("userToken", map1);
                Room room = userService.findRoomById(user.getId());
                if (room!=null&& !TextUtils.isEmpty(room.getId())){
                    map.put("roomId",room.getId());
                }
                /**********把token放到redis中*****************/
                map.put("uid", user.getId());
                map.put("name", user.getUsername());
                map.put("avatar", user.getUser_image());
                map.put("gender", "" + user.getSex());
                Response response = new Response("登录成功", JsonUtils.objectToJson(map), 200);
                return JsonUtils.objectToJson(response);
            }catch (Exception e){
                e.printStackTrace();
                Response response = new Response("系统错误，请稍后重试", null, 400);
                return JsonUtils.objectToJson(response);
            }finally {
                jedis.close();
            }
        }
    }


    //退出登录
    public Boolean logout(String user_id){
        Jedis jedis = jedispool.getResource();
        //删除键值对
        try {
            //清空token
            jedis.hdel("userToken",user_id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return false;
    }
}
