package com.kol_user.service;



import com.coin.dto.GiftList;
import com.coin.service.GiftOrderService;
import com.kol_friends.dto.Focus;
import com.kol_friends.service.FansService;
import com.kol_friends.service.FocusService;
import com.kol_friends.service.IntimacyService;


import com.kol_user.comment.IDUtil;


import com.kol_user.comment.JsonUtils;
import com.kol_user.comment.MD5;
import com.kol_user.dao.UserDao;
import com.kol_user.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserDao userdao;
    @Resource(name = "giftOrderServiceImpl")
    GiftOrderService giftOrderService;
    @Resource(name = "fansServiceImpl")
    FansService fansService;
    @Resource(name = "focusServiceImpl")
    FocusService focusService;
    @Resource(name = "intimacyServiceImpl")
    IntimacyService intimacyService;
    // @Resource(name = "userCoinsServiceImpl")
    // UserCoinsService userCoinsService;

    @Autowired
    JedisPool jedispool;

    public List findByProp(Map map){
        return userdao.findByProp(map);
    }
    public User findOneById(String id) {
        User user = userdao.findOneById(id);
        return user;
    }

    @Override
    public User findOneByIdentityId(String identityId) {
        User user = userdao.findOneByIdentityId(identityId);
        return user;
    }


    /////返回房间信息
    @Override
    public Room findRoomById(String roomId) {
        Room room = userdao.findRoomById(roomId);
        return room;
    }

    //返回用户基本信息
    @Override
    public List findInfosByListId(String[] id) {
        List list = userdao.findInfosByListId(id);
        return list;
    }

    @Override
    public Integer checkSexTime(String id) {
        Jedis jedis = jedispool.getResource();
        try {
            String time = "0";
            Integer index = 1;
            Map<String,String> map = new HashMap();
            map.put(id,time);
            jedis.hmset("sexTime",map);
            //判断key是否存在
            Boolean b = jedis.hexists("sexTime", id);
            if (b){
                if (Integer.valueOf(jedis.hmget("sexTime",id).get(0))<=1){
                    //次数自增1
                    jedis.hincrBy("sexTime",id,index);
                }else {
                    jedis.hset("sexTime",id,"2");
                }
                return Integer.valueOf(jedis.hmget("sexTime",id).get(0));
            }else {
                jedis.hset("sexTime",id,"1");
                // jedis.hincrBy("sexTime",id,index);
                return Integer.valueOf(jedis.hmget("sexTime",id).get(0));
            }
        }catch (Exception e){
            e.printStackTrace();
            return 3;
        }finally {
            jedis.close();
        }
    }

    @Override
    public User findOneByTelephone(String telephone) {
        User user=userdao.findOneByTelephone(telephone);
        return user;
    }

    @Override
    public List topAdvertisement(){
        return null;
    }

    //    查看粉丝列表
    public List findKolFans(){
//        调用交友模块的粉丝列表
        return null;
    }

    //个人主页的修改
    @Override
    public boolean updateImformation(User user){
//        进行修改操作
        return userdao.update(user);
        ///////////////////////////////////////////////
        // return false;
    }
    //    修改用户绑定的手机
    public void updateTelephone(String id,String telephone) {
        User user = userdao.findOneById(id);
        user.setTelephone(telephone);
        userdao.update(user);
    }

    //添加手机号，密码(后台自动生成用户ID)
    public void addTelephone(String telephone, String password) {
        ////////////////////////////////////////////////
        User user = new User();
        String id = IDUtil.createAppCode();
        String username = "用户_"+String.valueOf((Math.random()*9+1)*10000).substring(0,5);
        user.setId(id);
        user.setTelephone(telephone);
        user.setPassword(password);
        user.setUsername(username);
        /************添加用户到user_coin表中*************/
        UserCoins userCoins = new UserCoins();
        userCoins.setUser_id(user.getId());
        userdao.addUser(userCoins);
        // userCoinsService.addUser(userCoins);
        userdao.add(user);
    }

    //添加


    //新增用户
    public void add(User user) {
        userdao.add(user);
    }

    //校验手机号是否注册
    public boolean checkTelephone(String telephone) {
        try {
            User user = userdao.findOneByTelephone(telephone);
            //判断user对象是否为空
            Boolean isTrue = user==null;
            if (isTrue){
                //返回true，表示可注册
                return true;
            }
            // return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//     //    个人主页的的展示
//     @Override
//     public String showUserPage(String user_id){
//         User user=userdao.findOneById(user_id);
//         UserPageInformation userPageInformation = new UserPageInformation();
//         userPageInformation.setUid(user.getId());
//         userPageInformation.setSignature(user.getIntroduction());
//         userPageInformation.setAvatar(user.getUser_image());
//         userPageInformation.setName(user.getUsername());
//         userPageInformation.setSex(user.getSex());
//         userPageInformation.setAge(user.getAge());
//         userPageInformation.setHeight(user.getHeight());
//         userPageInformation.setPosition(user.getPosition());
//         userPageInformation.setHobby(user.getHobby());
//         //查看用户送出的礼物与收到的礼物
//         String giftList= giftOrderService.findGiftListByUser(user_id);
//         Map map=new HashMap();
// /********************加上粉丝数量和关注数量***************************************/
//         String fansNum = fansService.getFansNum(user_id);
//         String focusNum = focusService.getFocusNum(user_id);
// /********************加上粉丝数量和关注数量***************************************/
//         map.put("user", userPageInformation);
//         map.put("fansNum",fansNum);
//         map.put("focusNum",focusNum);
//         map.put("giftList",giftList);
//         return JsonUtils.objectToJson(map);
//     }

    @Override
    //忘记密码（重置密码）
    public String forgetPassword(String telephone,String rdNum, String password) {
        Jedis jedis=jedispool.getResource();
        try {
            boolean isTrue=rdNum.equals(jedis.get(telephone));
            if (isTrue){
                User user = userdao.findOneByTelephone(telephone);
                //MD5加密
                String md5Password = MD5.getEncryptedPwd(password);
                user.setPassword(md5Password);
                userdao.update(user);
                Response response = new Response("重置密码成功",null,200);
                return JsonUtils.objectToJson(response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        Response response = new Response("重置密码失败",null,308);
        return JsonUtils.objectToJson(response);
    }


    //访问好友个人主页
    @Override
    public String visitUserPage(String user_id,String targetUser_id){
        Jedis jedis = jedispool.getResource();
        try {
        User user=this.findOneById(targetUser_id);
        UserPageInformation userPageInformation = new UserPageInformation();
        userPageInformation.setUid(user.getId());
        userPageInformation.setSignature(user.getIntroduction());
        userPageInformation.setAvatar(user.getUser_image());
        userPageInformation.setName(user.getUsername());
        userPageInformation.setSex(user.getSex());
        userPageInformation.setAge(user.getAge());
        userPageInformation.setHeight(user.getHeight());
        userPageInformation.setPosition(user.getPosition());
        userPageInformation.setHobby(user.getHobby());
        //修改性别的次数
        Boolean b = jedis.hexists("sexTime", targetUser_id);
        String sexTime = "0";
        if (b){
            if (Integer.valueOf(jedis.hmget("sexTime",targetUser_id).get(0))>1){
                sexTime = "1";
            }else {
                sexTime = "1";
            }
        }
        userPageInformation.setSexTime(sexTime);
        //是否是关注对象
        Focus focus = focusService.getOnlyFocus(user_id, targetUser_id);
        Integer isFocus = 0;
        if (focus!=null){
            isFocus=1;
        }else {
            isFocus=0;
        }
        /*********************加上亲密度*********************************/
        String intimacy = intimacyService.getIntimacyValue(user_id, targetUser_id);
        /*********************加上亲密度*********************************/
        //收到的礼物列表
        List giftList = giftOrderService.findGiveGiftByUserToUser(targetUser_id);
        /*********************收到新礼物提示置0**************/
        if (user_id.equals(targetUser_id)) {
            jedis.hset("hasNewGift",user_id,"0");
        }
        Map map=new HashMap();
        map.put("targetUser",userPageInformation);
        map.put("intimacy",intimacy);
        map.put("isFocus",isFocus);
        map.put("giftList",giftList);
        return JsonUtils.objectToJson(map);
        }catch (Exception e){
            e.printStackTrace();
            return "系统出错";
        }finally {
            jedis.close();
        }
    }


    // //判断是否收到新礼物
    // public void isAcceptNewGift(String id){
    //     Jedis jedis = jedispool.getResource();
    //     try {
    //         String index = "0";
    //         Map<String,String> map = new HashMap();
    //         map.put(id,index);
    //         jedis.hmset("isNewGift",map);
    //         //判断key是否存在
    //         Boolean b = jedis.hexists("isNewGift", id);
    //         if (b){
    //             return;
    //         }else {
    //             jedis.hset("isNewGift",id,"0");
    //         }
    //     }catch (Exception e){
    //         e.printStackTrace();
    //     }finally {
    //         jedis.close();
    //     }
    // }





    //获取用户基本信息
    public String getUserInfos(String id){
        Jedis jedis = jedispool.getResource();
        try {
            String hasNewGift;
            /********1为有新礼物，0为无新礼物****/
            if (jedis.hexists("hasNewGift",id)){
                hasNewGift = jedis.hget("hasNewGift", id);
            }else {
                jedis.hset("hasNewGift", id,"0");
                hasNewGift = jedis.hget("hasNewGift", id);
            }
            //调用friends模块查询关注、粉丝数量
            String followCount = String.valueOf(focusService.getFocusList(id).size());
            String fansCount = String.valueOf(fansService.getFansList(id).size());
            Map map = new HashMap();
            map.put("fansCount",fansCount);
            map.put("followCount",followCount);
            map.put("hasNewGift",hasNewGift);
            return JsonUtils.objectToJson(map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }

    //模糊搜索用户列表
    @Override
    public List searchUserByKeyWord(String keyWord) {
        List list = userdao.searchUserByKeyWord(keyWord);
        return list;
    }


    //获取用户个人资料
    @Override
    public String getUserInfosByUser(String user_id,String uid) {
        Jedis jedis = jedispool.getResource();
        try {
            User user=this.findOneById(uid);
            Room room = this.findRoomById(uid);
            //返回房间信息
            String roomId = null;
            if (room!=null&&!room.getId().equals("")){
                roomId = room.getId();
            }
            //返回用户基本信息
            UserInfos userInfos = new UserInfos();
            userInfos.setUid(user.getId());
            userInfos.setSignature(user.getIntroduction());
            userInfos.setAvatar(user.getUser_image());
            userInfos.setName(user.getUsername());
            //是否是关注对象
            Focus focus = focusService.getOnlyFocus(user_id, uid);
            Integer isFocus = 0;
            if (focus!=null){
                isFocus=1;
            }else {
                isFocus=0;
            }
            //调用friends模块查询关注、粉丝数量
            String followCount = String.valueOf(focusService.getFocusList(uid).size());
            String fansCount = String.valueOf(fansService.getFansList(uid).size());

            //送出的礼物总数量
            int giveCount = 0;
            List giveGiftList = giftOrderService.findGiveGiftByUser(uid);
            for (int i = 0;i<giveGiftList.size();i++){
                GiftList giftList = (GiftList) giveGiftList.get(i);
                giveCount+=giftList.getCount();
            }
            //收到的礼物总数量
            int acceptCount = 0;
            List acceptGiftList = giftOrderService.findAcceptGiftByUser(uid);
            for (int i = 0;i<acceptGiftList.size();i++){
                GiftList giftList = (GiftList) acceptGiftList.get(i);
                acceptCount+=giftList.getCount();
            }
            Map map=new HashMap();
            map.put("targetUser",userInfos);
            map.put("isFocus",isFocus);
            map.put("giveCount",giveCount);
            map.put("acceptCount",acceptCount);
            map.put("fansCount",fansCount);
            map.put("followCount",followCount);
            map.put("roomId",roomId);
            return JsonUtils.objectToJson(map);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }


    //新增举报信息
    public boolean addReportInformation(ReportInformation reportInformation){
        return userdao.addReportInformation(reportInformation);
    }

    @Override
    public boolean addRemark(Remark remark) {
        return userdao.addRemark(remark);
    }

    @Override
    public Remark searchUserRemark(String user_id, String uid) {
        return userdao.searchUserRemark(user_id, uid);
    }

    @Override
    public boolean updateRemark(Remark remark) {
        return userdao.updateRemark(remark);
    }

    @Override
    public boolean updateRoomUsername(Room room) {
        return userdao.updateRoomUsername(room);
    }
}
