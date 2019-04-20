package com.kol_room.service;

import com.alibaba.fastjson.JSONObject;
import com.coin.dto.Gift;
import com.coin.dto.GiftOrder;
import com.coin.service.GiftOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kol_friends.dto.Focus;
import com.kol_friends.service.FocusService;
import com.kol_friends.service.IntimacyService;
import com.kol_room.comment.HttpRequest;
import com.kol_room.comment.JsonUtils;
import com.kol_room.dao.RoomDao;
import com.kol_room.dto.Response;
import com.kol_room.dto.ResponseSignal;
import com.kol_room.dto.Room;
import com.kol_room.dto.UserDetail;
import com.kol_user.dto.User;
import com.kol_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserRoomServiceImpl implements UserRoomService {
    @Resource
    private JedisPool jedisPool;
    @Autowired
    private SignalingService signalingService;
    @Resource
    private GiftOrderService giftOrderService;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private UserService userService;
    @Autowired
    private FocusService focusService;
    @Autowired
    private IntimacyService intimacyServiceImpl;


    //关键字搜索房间
    @Override
    public List searchRoomByKeyWord(String keyWord) {
        List list = roomDao.searchRoomByKeyWord(keyWord);
        return list;
    }

    /**************给予礼物**********************/
    //  送礼物
    public Response giveGift(List<GiftOrder> giftOrders, String room_id, String gift_id, String giver_id, String[] acceptIds) {
        System.out.println(giftOrders.toString());
        String new_room;
        GiftOrder giftOrder = new GiftOrder();
        Jedis jedis = jedisPool.getResource();
        try {
            int totalValue = 0;
            for (int i = 0; i < giftOrders.size(); i++) {
                giftOrder = giftOrders.get(i);
                if (getOneUser(giftOrder.getAccept_id()) != null) {
                    System.out.println("有这个人");
                } else {
                    giftOrders.remove(i);
                    i = i - 1;
                }
            }
//        获取礼物的价格
            /******************根据id查询礼物*********************/
            if (giftOrders.size() > 0) {
                Gift gift = giftOrderService.findGiftById(gift_id);
                for (int i = 0; i < giftOrders.size(); i++) {
                    giftOrder = giftOrders.get(i);
                    //增加亲密度
                    intimacyServiceImpl.byGift(giftOrder.getAccept_id(), giver_id, giftOrder.getTotalValue());
                    giftOrder.setUnitPrice(gift.getValue());
                    System.out.println("礼物价值==========" + gift.getGift_value() + ";num====>" + giftOrder.getGift_num());
                    giftOrder.setTotalValue(gift.getValue() * giftOrder.getGift_num());
                    giftOrder.setCharmValue(gift.getCharm_value());
                    giftOrder.setTotalCharmValue(gift.getCharm_value() * giftOrder.getGift_num());
                    //获得所有用户的礼物的价格的总和
                    totalValue += giftOrder.getTotalValue();
                    giftOrders.set(i, giftOrder);
                }

                /********************判断赠送礼物是否成功*********************/
                Boolean isSuccess = giftOrderService.giveGift(giftOrders, gift_id, giver_id, totalValue);
//        插入赠送礼物的记录
                if (isSuccess != null && isSuccess) {
                    String isOpen = jedis.get("Counter" + room_id);
                    if (isOpen == null) {
                        jedis.set("Counter" + room_id, "0");
                    }
                    if (isOpen != null && isOpen.equals("1")) {
                        String room = jedis.get("room_" + room_id);
                        System.out.println("room===<<>>===" + room);
                        List<Map<String, Object>> microphones = JsonUtils.jsonToPojo(room, List.class);
                        if (microphones != null) {
                            for (int i = 0; i < microphones.size(); i++) {
                                int totalCharmValue = gift.getGift_value() * giftOrders.get(0).getGift_num();
                                Map<String, Object> map = microphones.get(i);
                                System.out.println("totalCharmValue====" + totalCharmValue);
                                Map<String, Object> user = (Map<String, Object>) map.get("user");
                                if (!map.isEmpty() && user != null && user.get("uid") != null && Arrays.asList(acceptIds).contains(user.get("uid"))) {
                                    System.out.println("user===" + ((Map) map.get("user")).get("pkValue") + ";totalCharmValue====" + totalCharmValue);
                                    user.put("pkValue", (int) ((Map) map.get("user")).get("pkValue") + totalCharmValue);
                                    map.put("user", user);
                                    microphones.set(i, map);
                                    new_room = JsonUtils.objectToJson(microphones);
                                    jedis.set("room_" + giftOrder.getRoom_id(), new_room);
                                }
                            }
                        }
                        /****获取接收者id,增加一个收到新礼物的通知（用户主页的红点展示）****/
                        jedis.hset("hasNewGift", giftOrder.getAccept_id(), "1");
                        System.out.println(giftOrder.getAccept_id() + "========" + jedis.hget("hasNewGift", giftOrder.getAccept_id()));
                        /*******************应该返回200赠送成功，并且；礼物值并没有发生变化******************/
                        /*通过信道通知PK值的变化*/
                        Response response = new Response("赠送成功", 200);
                        ResponseSignal map = new ResponseSignal("100", microphones);
                        this.signalingMsg(room_id, JsonUtils.objectToJson(map), "00000000");
                        return response;
                    } else {
                        /****************增加一个收到新礼物的通知（用户主页的红点展示）***********/
                        for (int i = 0; i < giftOrders.size(); i++) {
                            giftOrder = giftOrders.get(i);
                            jedis.hset("hasNewGift", giftOrder.getAccept_id(), "1");
                            System.out.println(giftOrder.getAccept_id() + "========" + jedis.hget("hasNewGift", giftOrder.getAccept_id()));
                        }
                        return new Response("赠送成功", 200);
                    }
                } else if (isSuccess == null) {
                    return new Response("系统出错,请稍后重试", 400);
                } else {
                    return new Response("余额不足", 400);
                }
            } else {
                return new Response("未找到赠送对象", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错,请稍后重试", 400);
        } finally {
            jedis.close();
        }
    }


    @Override
    public User getOneUser(String user_id) {
        return userService.findOneById(user_id);
    }

    //    创建房间
    public Response createRoom(String user_id, String room_label, String limit, String room_name, String room_topic) {
        Jedis jedis = jedisPool.getResource();
        Response response;
        try {
            Room room1 = roomDao.findOneById(user_id);
            if (room1 != null) {
                response = new Response("房间已存在", 400);
            } else {
                User user = userService.findOneById(user_id);
                Room room = new Room();
                room.setId(user.getId());
                room.setRoom_topic(room_topic);
                room.setState(1);
                room.setRoom_name(room_name);
                room.setRoom_limit(Integer.valueOf(limit));
                room.setUsername(user.getUsername());
                // room.setUser_image(user.getUser_image());
                room.setRoom_label(room_label);
                room.setRoom_position(1);
                int i = (int) (Math.random() * 3);
                room.setRoom_image("http://129.204.52.213/room_image/bg_room_" + i + ".png");
                boolean b = roomDao.createRoom(room);
                if (b) {
                    jedis.set("screen_" + room.getId(), "1");
                    jedis.set("Counter" + room.getId(), "0");
                    Map map = new HashMap();
                    map.put("roomId", user_id);
                    response = new Response("创建房间成功", JsonUtils.objectToJson(map), 200);
                } else {
                    response = new Response("创建房间失败", 400);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("创建房间失败", 400);
        } finally {
            jedis.close();
        }
        return response;
    }

    //    得到在麦位上的用户头像和名字
    public UserDetail getUser(String user_id, int position) {
//        将用户模块封装的user拿过来然后抽出三项填入user_phone
        User user = userService.findOneById(user_id);
        UserDetail userDetail = new UserDetail();
        userDetail.setUser_id(user_id);
        userDetail.setUser_image(user.getUser_image());
        userDetail.setUsername(user.getUsername());
//        user.setUsername();
//        user.setUser_image();
        userDetail.setPosition(position);
        return userDetail;
    }

    //    上麦,需要传入用户id，麦位，房间id，要通过信道通知其他用户
    public Response up_phone(String user_id, int position, String room_id, String channelName) {
        //获取user对象（包括头像，昵称，id，麦位，魅力值）
        UserDetail user = getUser(user_id, position);
        //麦克风对象
        Jedis jedis = jedisPool.getResource();
        try {
            List list = JsonUtils.jsonToPojo(jedis.get("room_" + room_id), List.class);
            //设置房间号
//        List<Map<String, Object>> list = new ArrayList<>(10);
            if (list == null || list.size() < 1) {
                list = new ArrayList<Map<String, Object>>();
                System.out.println(list.size());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
            }
            Map<String, Object> user1 = new HashMap<>();
            user1.put("uid", user.getUser_id());
            user1.put("avatar", user.getUser_image());
            user1.put("name", user.getUsername());
            user1.put("pkValue", 0);
            String s1 = jedis.get("roomAdmin_" + room_id + user.getUser_id());
            if (s1 != null && s1.length() > 0) {
                user1.put("role", 2);
            } else {
                user1.put("role", 0);
            }
            Map<String, Object> map = new HashMap<>();
            if (position >= 0 && ((Map) list.get(position)).isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map1 = (Map<String, Object>) list.get(i);
                    if (!map1.isEmpty() && ((int) map1.get("status") == 1 || (int) map1.get("status") == 3)) {
                        Map<String, Object> user2 = (Map<String, Object>) map1.get("user");
                        if (user2 != null) {
                            String uid = (String) user2.get("uid");
                            if (uid.equals(user_id)) {
                                map1.remove("user");
                                if ((int) map1.get("status") == 1) {
                                    map1.put("status", 0);
                                }
                                list.set(i, map1);
                            }
                        }
                    }
                }
                map.put("user", user1);
                map.put("position", position);
                map.put("status", 1);
                list.remove(position);
                list.add(position, map);
                jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
                /**************通过信令通知上麦*****************/
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("position", position);
                ResponseSignal responseSignal = new ResponseSignal("100", list);
                String msg = JsonUtils.objectToJson(responseSignal);
                /**************通过信令通知上麦*****************/
                this.signalingMsg(channelName, msg, "00000000");
                return new Response("上麦成功", null, 200);
            } else if ((int) ((Map) list.get(position)).get("status") == 2) {
                return new Response("麦位已被封,上麦失败", null, 400);
            } else if ((int) ((Map) list.get(position)).get("status") == 0 || (int) ((Map) list.get(position)).get("status") == 3) {
                map.put("status", 1);
                if ((int) ((Map) list.get(position)).get("status") == 3) {
                    map.put("status", 3);
                }
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map1 = (Map<String, Object>) list.get(i);
                    if (!map1.isEmpty() && ((int) map1.get("status") == 1 || (int) map1.get("status") == 3)) {
                        Map<String, Object> user2 = (Map<String, Object>) map1.get("user");
                        if (user2 != null) {
                            String uid = (String) user2.get("uid");
                            System.out.println("uid========" + uid);
                            if (uid.equals(user_id)) {
                                int pkValue = (int) user2.get("pkValue");
                                user1.put("pkValue", pkValue);
                                map1.remove("user");
                                if ((int) map1.get("status") == 1) {
                                    map1.put("status", 0);
                                } else if ((int) map1.get("status") == 3) {
                                    map1.put("status", 3);
                                }
                            }
                        }
                    }
                }
                map.put("user", user1);
                map.put("position", position);
                list.remove(position);
                list.add(position, map);
                jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
                /**************通过信令通知上麦*****************/
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("position", position);
                ResponseSignal responseSignal = new ResponseSignal("100", list);
                String msg = JsonUtils.objectToJson(responseSignal);
                /**************通过信令通知上麦*****************/
                this.signalingMsg(channelName, msg, "00000000");
                return new Response("上麦成功", null, 200);
            } else {
                return new Response("麦位上有人,上麦失败", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错,请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void invite(String user_id) {
        Map<Object, Object> map = new HashMap<>();
        map.put("cmd", "102");
        this.signalingMsgOneToOne(JsonUtils.objectToJson(map), user_id);
    }

    //下麦，要通过信道通知其他用户该用户已经下麦，然后查询排麦的第一位，如果有人排麦，就发信道过去
    public Response down_phone(String room_id, String user_id) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<Map<String, Object>> microphones = JsonUtils.jsonToPojo(jedis.get("room_" + room_id), List.class);
            if (microphones != null) {
                for (int i = 0; i < microphones.size(); i++) {
                    if (microphones.get(i) != null && microphones.get(i).get("user") != null && ((Map) microphones.get(i).get("user")).get("uid").equals(user_id)) {
                        microphones.get(i).remove("user");
                        if ((int) microphones.get(i).get("status") == 1) {
                            microphones.get(i).put("status", 0);
                        }
                        //microphones.set(i, );
                        ResponseSignal responseSignal = new ResponseSignal("100", microphones);
                        String msg = JsonUtils.objectToJson(responseSignal);
                        this.signalingMsg(room_id, msg, "00000000");
                        // 查看当前房间是否有人在排麦
                        Map map;
                        Response response = this.platoonList(room_id);
                        if (response.getData().length() > 0 && !response.getData().equals("[]")) {
                            Set<String> set = JsonUtils.jsonToPojo(response.getData(), Set.class);
                            //        如果有人在排麦，就用信道给那个排麦第一名的人发点对点消息
                            map = JsonUtils.jsonToPojo(set.iterator().next(), Map.class);
                            String user_id1 = (String) map.get("user_id");
                            Response platoonSignal = new Response(user_id1 + "排麦成功", null, 108);
                            msg = platoonSignal.toString();
                            //利用信道去通知
                            this.signalingMsgOneToOne(msg, user_id1);
                        }
                        jedis.set("room_" + room_id, JsonUtils.objectToJson(microphones));
                        return new Response("下麦成功", null, 200);
                    }
                }
                return new Response("下麦成功", null, 200);
            } else {
                System.out.println("麦上没人");
                return new Response("麦上没人", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }


    //查询是否在线,在线-发点对点,不在线-清麦位信息
    @Override
    public void request_down_phone(String roomId, int position, String uid) {
        Jedis jedis = jedisPool.getResource();
        String s1 = jedis.get("room_" + roomId);
        try {
            if (s1 != null) {
                List<Map> maps = JsonUtils.jsonToList(s1, Map.class);
                List<Map> maps1 = JsonUtils.jsonToList(s1, Map.class);
                if (maps1 != null && maps1.get(position).get("user") != null) {
                    Map<String, Object> user = (Map<String, Object>) maps.get(position).get("user");
                    String uid1 = (String) user.get("uid");
                    boolean status = signalingService.getStatus(roomId, uid1);
                    if (status) {
                        //在线-发点对点
                        Map<Object, Object> map = new HashMap<>();
                        map.put("cmd", "101");
                        this.signalingMsgOneToOne(JsonUtils.objectToJson(map), uid1);
                    } else {
                        //不在线-清麦位信息
                        String s = jedis.get("room_" + roomId);
                        if (s != null) {
                            List list = JsonUtils.jsonToPojo(s, List.class);
                            list.set(position, new HashMap<>());
                            jedis.set("room_" + roomId, JsonUtils.objectToJson(list));
                            this.signalingMsg(roomId, JsonUtils.objectToJson(list), "00000000");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    //    房间在线人列表
    public Response showRoomPeopleList(String room_id) {
        Jedis jedis = jedisPool.getResource();
        String data = "";
        try {
            Room room = roomDao.findOneById(room_id);
            if (room != null) {
                Map<String, List<Integer>> peopleList = signalingService.getPeopleList(room_id);
                List<Map<String, Object>> list = new ArrayList<>();
                User user = userService.findOneById(room_id);
                if (peopleList != null) {
                    List<Integer> audience = peopleList.get("audience");
                    List<Integer> broadcasters = peopleList.get("broadcasters");
                    if (audience != null && audience.size() > 0) {
                        for (int i = 0; i < audience.size(); i++) {
                            Map<String, Object> map1 = new HashMap<>();
                            String s = audience.get(i) + "";
                            System.out.println("userId===" + s);
                            User user1 = userService.findOneById(s);
                            map1.put("uid", user1.getId());
                            map1.put("name", user1.getUsername());
                            map1.put("avatar", user1.getUser_image());
                            String s1 = jedis.get("roomAdmin_" + room_id + user1.getId());
                            if (s1 != null && s1.length() > 0) {
                                map1.put("role", 2);
                            } else {
                                map1.put("role", 0);
                            }
                            map1.put("isMicro", 0);
                            map1.put("signature", user1.getIntroduction());
                            list.add(map1);
                        }
                    }
                    if (broadcasters != null && broadcasters.size() > 0) {
                        for (int i = 0; i < broadcasters.size(); i++) {
                            String s = broadcasters.get(i) + "";
                            if (s.equals(room_id)) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("uid", user.getId());
                                map.put("name", user.getUsername());
                                map.put("avatar", user.getUser_image());
                                map.put("role", 1);
                                map.put("isMicro", 0);
                                map.put("signature", user.getIntroduction());
                                list.add(map);
                            } else {
                                Map<String, Object> map2 = new HashMap<>();
                                User user2 = userService.findOneById(s);
                                map2.put("name", user2.getUsername());
                                map2.put("uid", user2.getId());
                                map2.put("avatar", user2.getUser_image());
                                String s1 = jedis.get("roomAdmin_" + room_id + user2.getId());
                                if (s1 != null && s1.length() > 0) {
                                    map2.put("role", 2);
                                } else {
                                    map2.put("role", 0);
                                }
                                map2.put("isMicro", 1);
                                map2.put("signature", user2.getIntroduction());
                                list.add(map2);
                            }
                        }
                    }
                }
                data = JsonUtils.objectToJson(list);
                return new Response("获取在线列表成功", data, 200);
            } else {
                return new Response("房间不存在", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    //    显示麦位信息
    public List show_microphone(String room_id) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<Map<String, Object>> list = JsonUtils.jsonToPojo(jedis.get("room_" + room_id), List.class);
            Map<String, List<Integer>> peopleList = signalingService.getPeopleList(room_id);
            if (peopleList != null && peopleList.get("broadcasters") != null && list != null && list.size() > 0) {
                System.out.println("来了=====显示麦位信息");
                List<Integer> broadcasters = peopleList.get("broadcasters");
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> user = (Map<String, Object>) list.get(i).get("user");
                    if (user != null) {
                        String uid = (String) user.get("uid");
                        System.out.println("uid=-=-=-=-=-=-=" + uid);
                        if (!broadcasters.contains(Integer.valueOf(uid))) {
                            System.out.println("1111111111");
                            Map<String, Object> map = list.get(i);
                            map.remove("user");
                            if ((int) list.get(i).get("status") == 1) {
                                map.put("status", 0);
                            }
                            list.set(i, map);
                        }
                    }
                }
            } else {
                list = new ArrayList<>();
                list.add(new HashMap<>());
                System.out.println(list.size());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
            }
            jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
            return JsonUtils.jsonToPojo(jedis.get("room_" + room_id), List.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

    //    封麦
    public Response ban_microphones(String room_id, int position) {
        //通过信令发送消息
        Jedis jedis = jedisPool.getResource();
        try {
            List<Map<String, Object>> list = JsonUtils.jsonToPojo(jedis.get("room_" + room_id), List.class);
            if (list != null) {
                Map<String, Object> map = new HashMap<>();
                if (!list.get(position).isEmpty() && (list.get(position).get("status")).equals(2)) {
                    map.put("position", position);
                    map.put("status", 0);
                    list.set(position, map);
                    jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
                    ResponseSignal responseSignal = new ResponseSignal("100", list);
                    String msg = JsonUtils.objectToJson(responseSignal);
                    this.signalingMsg(room_id, msg, "00000000");
                    return new Response("解除封麦成功", null, 200);
                }
                if (!list.get(position).isEmpty() && list.get(position).get("user") != null) {
                    Map<String, Object> user = (Map<String, Object>) list.get(position).get("user");
                    String uid = (String) user.get("uid");
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("cmd", "101");
                    this.signalingMsgOneToOne(JsonUtils.objectToJson(map3), uid);
                }

                //{"position": 0,"user":{"uid":"29128309","name":"69017","avatar":"http://129.204.52.213/user_image/ic_mine_head_man.png","pkValue": 0},"status": 1}
                map.put("position", position);
                map.put("status", 2);
                list.set(position, map);
                jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
                ResponseSignal responseSignal = new ResponseSignal("100", list);
                String msg = JsonUtils.objectToJson(responseSignal);
                this.signalingMsg(room_id, msg, "00000000");
            } else {
                list = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                map.put("position", position);
                map.put("status", 2);
                list.set(position, map);
                ResponseSignal responseSignal = new ResponseSignal("100", list);
                jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
                String msg = JsonUtils.objectToJson(responseSignal);
                this.signalingMsg(room_id, msg, "00000000");
            }
            return new Response("封麦成功", null, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 405);
        } finally {
            jedis.close();
        }
    }

    //    禁麦
    public Response prohibit_microphones(String room_id, int position) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<Map<String, Object>> list = JsonUtils.jsonToPojo(jedis.get("room_" + room_id), List.class);
            Map<String, Object> map;
            if (list != null && !list.get(position).isEmpty() && (int) (list.get(position)).get("status") != 2) {
                if (list.get(position).get("status").equals(3)) {
                    map = list.get(position);
                    Map<String, Object> user = (Map<String, Object>) map.get("user");
                    map.put("position", position);
                    if (user != null) {
                        map.put("status", 1);
                    } else {
                        map.put("status", 0);
                    }
                    map.put("user", user);
                    list.set(position, map);
                    jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
                    ResponseSignal responseSignal = new ResponseSignal("100", list);
                    String msg = JsonUtils.objectToJson(responseSignal);
                    this.signalingMsg(room_id, msg, "00000000");
                    return new Response("解除禁麦成功", null, 200);
                }
                map = list.get(position);
            } else if (list != null && !list.get(position).isEmpty() && (int) (list.get(position)).get("status") == 2) {
                return new Response("麦位已被封,禁麦失败", null, 400);
            } else if (list != null) {
                map = new HashMap<>();
            } else {
                list = new ArrayList<>();
                map = new HashMap<>();
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
                list.add(new HashMap<>());
            }
            map.put("position", position);
            map.put("status", 3);
            list.set(position, map);
            jedis.set("room_" + room_id, JsonUtils.objectToJson(list));
            ResponseSignal responseSignal = new ResponseSignal("100", list);
            String msg = JsonUtils.objectToJson(responseSignal);
            this.signalingMsg(room_id, msg, "00000000");
            return new Response("禁麦成功", null, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    //    显示房间所有人的人数
    public Long showRoomPeopleNum(String room_id) {
        int num = 0;
        Jedis jedis = jedisPool.getResource();
        try {
            Map<String, List<Integer>> peopleList = signalingService.getPeopleList(room_id);
            if (peopleList != null) {
                if (peopleList.get("audience") != null) {
                    num += peopleList.get("audience").size();
                }
                if (peopleList.get("broadcasters") != null) {
                    num += peopleList.get("broadcasters").size();
                }
            }
            jedis.set("user_detail_number" + room_id, String.valueOf(num));
            return (long) num;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    //  房间人数新增
    public void addRoomPeople(String room_id, String user_id) {
        Jedis jedis = jedisPool.getResource();
        UserDetail userDetail = new UserDetail();
//      通过user_id调用用户模块来获取用户信息
        User user = userService.findOneById(user_id);
        userDetail.setUser_id(user_id);
        userDetail.setUser_image(user.getUser_image());
        userDetail.setUsername(user.getUsername());
        userDetail.setCharmValue(0);
        try {
            jedis.set("user_detail" + user_id, JsonUtils.objectToJson(userDetail));
            jedis.sadd("room_people_" + room_id, "user_detail" + user_id);
            String num = jedis.get("user_detail_number" + room_id);
            if (num != null) {
                jedis.set("user_detail_number" + room_id, Integer.valueOf(num) + 1 + "");
            } else {
                jedis.set("user_detail_number" + room_id, 1 + "");
            }
        } finally {
            jedis.close();
        }
    }

    //    房间人数减少
    public void deleteRoomPeople(String room_id, String user_id) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.srem("room_people_" + room_id, "user_detail" + user_id);
            String num = jedis.get("user_detail_number" + room_id);
            if (num != null) {
                jedis.set("user_detail_number" + room_id, Integer.valueOf(num) - 1 + "");
            } else {
                jedis.set("user_detail_number" + room_id, 0 + "");
            }
        } finally {
            jedis.close();
        }
    }

    //踢出房间
    public Response getOutRoom(String cname, String uid, String room_id) {
        String appid = "479bd1272f9441f5ac59d2750bb817ce";
        int time = 60;
        JSONObject agoraRuleObject = new JSONObject();
        agoraRuleObject.put("appid", appid);
//        频道名
        agoraRuleObject.put("cname", cname);
        agoraRuleObject.put("uid", uid);
        agoraRuleObject.put("time", time);
//        agoraRuleObject.put("ip","192.168.12.93");
        String agoraRule = JSONObject.toJSONString(agoraRuleObject);
        System.out.println(agoraRule);
//        然后再创建规则
        String agoraUrl = "https://api.agora.io/dev/v1/kicking-rule/";
        String agoraResult = HttpRequest.sendPost(agoraUrl, agoraRule);
        System.out.println(agoraResult);
//        减少房间人数
        deleteRoomPeople(room_id, uid);
//        调用信道发消息
        Response response = new Response(uid + "已被踢出房间", 100);
        String msg = JsonUtils.objectToJson(response);
        this.signalingMsg(cname, msg, "00000000");
        return response;
    }

    //频道消息
    public void signalingMsg(String channelName, String msg, String account) {
        //登录信令
        signalingService.login(account);
        //加入频道
        signalingService.joinChannel(channelName, account);
        //发送消息
        signalingService.channelDeal(msg, account);
    }

    //    一对一信道
    public void signalingMsgOneToOne(String msg, String user_id) {
        signalingService.sendMsg(msg, user_id);
    }

    ////    飞机票的使用
//    public void userPlaneTicket(String user_id,String currentRoom,String targetRoom){
////        目标房间增加人数
//        this.addRoomPeople(targetRoom,user_id);
////        当前房间减少人数
//        this.deleteRoomPeople(currentRoom,user_id);
////        用信道通知目标房间有人进入了房间
//    }

    //    进入房间
    public Response joinRoom(String room_id, String user_id) {
//        先去查询该用户是否能够进入房间的权限
        Room room = roomDao.findOneById(room_id);
        if (room != null) {
            if (room.getState() != 0) {

//            如果房间的state不为0，说明可以进入该用户的房间
                this.addRoomPeople(room_id, user_id);
//        用信令通知房间里面有人进入了
//            this.signalingMsg(room_id, "用户" + user_id + "进入房间", user_id);
                return new Response("成功进入房间", 200);
            } else {
                return new Response("没有权限进入房间", 400);
            }
        } else {
            return new Response("房间不存在", 400);
        }
    }

    //    房间列表
    public Response selectRoomList(int label, int pageNo) {
        int pageSize = 10;
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<Map> list = new ArrayList<>();
        Room room;
        Response response = new Response();
        Jedis jedis = jedisPool.getResource();
        List roomList;
        /******************************获取房间列表*********************************/
        try {
            if (label > 0) {
                roomList = roomDao.selectRoomList(label);
            } else {
                roomList = roomDao.selectRoomAllList();
            }

            Room[] rooms = new Room[roomList.size()];
            for (int i = 0; i < roomList.size(); i++) {
                room = (Room) roomList.get(i);
                System.out.println(room);
                //jedis.scard返回长度
                String num = jedis.get("user_detail_number" + room.getId());
                // System.out.println(jedis.get("room_id"));
//                System.out.println(room.getId() + "=====" + num);
//                Map<String, List<Integer>> peopleList = signalingService.getPeopleList(room.getId());
//                int num = 0;
//                if (peopleList != null) {
//                    if (peopleList.get("broadcasters") != null) {
//                        num += peopleList.get("broadcasters").size();
//                    }
//                    if (peopleList.get("audience") != null) {
//                        num += peopleList.get("audience").size();
//                    }
//                }
                if (num != null) {
                    room.setRoom_number(Long.valueOf(num));
                } else {
                    room.setRoom_number(0L);
                }
                rooms[i] = room;
            }
            List<Room> rooms1 = sort(rooms);
            List<Room> rooms2 = new ArrayList<>();
            for (int s = 0; s < rooms1.size(); s++) {
                if (rooms1.get(s).getRoom_position() == 0 && s > 0) {
                    rooms2.add(rooms1.get(s));
                    rooms1.remove(s);
                }
            }
            List<Room> rooms3 = new ArrayList<>();
            rooms3.addAll(rooms2);
            rooms3.addAll(rooms1);
            for (int j = 0; j < rooms3.size(); j++) {
                room = rooms3.get(j);
                User user = userService.findOneById(room.getId());
                System.out.println("roomId========" + room.getId());
                System.out.println("user===========" + user);
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map1 = new HashMap<>();
                map1.put("uid", user.getId());
                map1.put("name", user.getUsername());
                map1.put("avatar", user.getUser_image());
                map.put("id", room.getId());
                map.put("name", room.getRoom_name());
                map.put("type", room.getRoom_type());
                map.put("tag", room.getRoom_topic());
                map.put("background", room.getRoom_image());
                map.put("owner", map1);
                list.add(map);
            }
            if(pageNo<=page.getPages()) {
                PageInfo<Map> pageInfo = new PageInfo<>(list);
                response.setData(JsonUtils.objectToJson(pageInfo.getList()));
                response.setStatus(200);
                response.setMsg("获取房间列表成功");
            }else{
                response = new Response("获取房间列表结束","[]",200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", "[]", 400);
        } finally {
            jedis.close();
        }
        return response;
    }

    //  首页的搜索，用来搜索用户和房间
    public Map<String, String> searchUserAndRoom(String words) {
        List userList;
        List roomList;
        Map<String, String> map = new HashMap<>();
        map.put("room_name", words);
        map.put("username", words);
        roomList = roomDao.findByProp(map);
        userList = userService.findByProp(map);
        map.put("users", userList.toString());
        map.put("rooms", roomList.toString());
        return map;
    }

    //房间设置的修改
    public boolean updateRoom(Map map) {
//        this.signalingMsg((String) map.get("id"), JsonUtils.objectToJson(map), "00000000");
        return roomDao.updateRoom(map);
    }

    //    根据房间id来查找房间
    public Response findOneById(String roomId, String user_id) {
        Jedis jedis = jedisPool.getResource();
//      根据房间id来查找
        try {
            Room room = roomDao.findOneById(roomId);
            User user = userService.findOneById(roomId);
            Focus focus = focusService.getOnlyFocus(user_id, roomId);
            List<Map<String, Object>> microphones = show_microphone(roomId);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> owner = new HashMap<>();
            owner.put("uid", user.getId());
            owner.put("name", user.getUsername());
            owner.put("avatar", user.getUser_image());
            owner.put("isOnline", 0);
            if (signalingService.getStatus(roomId, roomId)) {
                owner.put("isOnline", 1);
            }
            String s1 = jedis.get("roomAdmin_" + roomId + user_id);
            if (s1 != null && s1.length() > 0) {
                map.put("role", 2);
            } else {
                map.put("role", 0);
            }
            if (user.getUsername().equals(room.getUsername())) {
                map.put("role", 1);
            }
            map.put("owner", owner);
            map.put("id", roomId);
            map.put("name", room.getRoom_name());
            map.put("background", room.getRoom_image());
            map.put("onlineCount", jedis.scard("room_people_" + roomId));//在线人数
            map.put("notice", room.getRoom_notice());//公告
            map.put("limit", room.getRoom_limit());//限制
            map.put("label", room.getRoom_label());//标签
            map.put("topic", room.getRoom_topic());//分类
            if (focus != null) {
                map.put("isFollow", 1);
            } else {
                map.put("isFollow", 0);
            }
            map.put("imStatus", jedis.get("screen_" + roomId));
            map.put("pkMode", jedis.get("Counter" + roomId));
            map.put("microphones", microphones);
            map.put("platoon", jedis.zcard("platoon" + roomId));
            return new Response("获取成功", JsonUtils.objectToJson(map), 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Response Counter(String roomId) {
        Jedis jedis = jedisPool.getResource();
        try {
//        redis记录该房间已经开启了计数器了
            String s = jedis.get("Counter" + roomId);
            String msg = "";
            String num = "";
            if (s != null) {
                if (s.equals("0")) {
                    System.out.println("开启计数器");
                    num = "1";
                    jedis.set("Counter" + roomId, num);
                    Map<String, Object> map = new HashMap<>();
                    map.put("cmd", 203);
                    this.signalingMsg(roomId, JsonUtils.objectToJson(map), "00000000");
                    msg = "开启计数器";
                } else {
                    num = "0";
                    System.out.println("关闭计数器");
                    jedis.set("Counter" + roomId, num);
                    Map<String, Object> map = new HashMap<>();
                    List<Map<String, Object>> microphones = show_microphone(roomId);
                    if (microphones != null) {
                        for (int i = 0; i < microphones.size(); i++) {
                            Map<String, Object> map1 = microphones.get(i);
                            Map<String, Object> user = (Map<String, Object>) map1.get("user");
                            if (user != null) {
                                user.put("pkValue", 0);
                                map1.put("user", user);
                            }
                            microphones.set(i, map1);
                        }
                        jedis.set("room_" + roomId, JsonUtils.objectToJson(microphones));
                        ResponseSignal responseSignal = new ResponseSignal("100", microphones);
                        this.signalingMsg(roomId, JsonUtils.objectToJson(responseSignal), "00000000");
                    }
                    map.put("cmd", "204");
                    this.signalingMsg(roomId, JsonUtils.objectToJson(map), "00000000");
                    msg = "关闭计数器";
                }
            } else {
                num = "1";
                jedis.set("Counter" + roomId, num);
                Map<String, Object> map = new HashMap<>();
                map.put("cmd", 203);
                msg = "开启计数器";
                this.signalingMsg(roomId, JsonUtils.objectToJson(map), "00000000");
            }
            return new Response(msg, num, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Response Screen(String roomId) {
        Jedis jedis = jedisPool.getResource();
        try {
            String s = jedis.get("screen_" + roomId);
            String msg = "";
            String num = "";

            if (s != null) {
                if (s.equals("0")) {
                    num = "1";
                    jedis.set("screen_" + roomId, num);
                    Map<String, Object> map = new HashMap<>();
                    map.put("cmd", "201");
                    this.signalingMsg(roomId, JsonUtils.objectToJson(map), "00000000");
                    msg = "公屏打开成功";
                } else {
                    num = "0";
                    jedis.set("screen_" + roomId, num);
                    Map<String, Object> map = new HashMap<>();
                    map.put("cmd", "202");
                    this.signalingMsg(roomId, JsonUtils.objectToJson(map), "00000000");
                    msg = "公屏关闭成功";
                }
            } else {
                num = "0";
                jedis.set("screen_" + roomId, num);
                Map<String, Object> map = new HashMap<>();
                map.put("cmd", "202");
                msg = "公屏关闭成功";
                this.signalingMsg(roomId, JsonUtils.objectToJson(map), "00000000");
            }
            return new Response(msg, num, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    //房间预读
    @Override
    public Map<String, Object> preInfo(String roomId, String userId) {
        Map<String, Object> map = new HashMap<>();
        Jedis jedis = jedisPool.getResource();
        try {
            Room room = roomDao.findOneById(roomId);
            Map<String, Object> map1 = new HashMap<>();
            if (room != null) {
//        List<Map<String,Object>> list = new ArrayList<>();
                User user = userService.findOneById(room.getId());
                map.put("uid", user.getId());
                map.put("name", user.getUsername());
                map.put("avatar", user.getUser_image());
//        list.add(map);
                map1.put("owner", map);
                map1.put("roomName", room.getRoom_name());
                map1.put("isOpen", 1);
                map1.put("isLimit", room.getRoom_limit());
                map1.put("label", room.getRoom_label());//标签
                map1.put("topic", room.getRoom_topic());//分类
                map1.put("isBlocked", 0);
                map1.put("roomImage", room.getRoom_image());
                String agaToken = SignalingService.SimpleTokenBuilder(room.getId(), userId);
                String signalToken = SignalingService.getToken(userId);
                map1.put("agaToken", agaToken);
                map1.put("signalToken", signalToken);
                String s = jedis.get("roomAdmin_" + roomId + userId);
                if (s != null && s.length() > 0) {
                    map1.put("isAdmin", 1);
                } else {
                    map1.put("isAdmin", 0);
                }
            }
            return map1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

    //    用户进行排麦,取消排麦
//    参数有user_id,user_image,username,introduction
    public Response platoon(Map map, String room_id) {
        Jedis jedis = jedisPool.getResource();
        try {
            String param = JsonUtils.objectToJson(map);
            Double zscore = jedis.zscore("platoon" + room_id, param);
            //有历史数据
            if (zscore != null && zscore > 0) {
                long member = jedis.zcard("platoon" + room_id);
                jedis.zrem("platoon" + room_id, param);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("cmd", "205");
                map2.put("data", member - 1);
                this.signalingMsg(room_id, JsonUtils.objectToJson(map2), "00000000");
                return new Response("取消排麦成功", null, 200);
            } else {
                long member = jedis.zcard("platoon" + room_id);
                jedis.zadd("platoon" + room_id, member + 1, param);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("cmd", "205");
                map2.put("data", member + 1);
                this.signalingMsg(room_id, JsonUtils.objectToJson(map2), "00000000");
                return new Response("排麦成功", null, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    //    排麦列表
    public Response platoonList(String room_id) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = "platoon" + room_id;
            Map<String, List<Integer>> peopleList = signalingService.getPeopleList(room_id);
            String platoon = "[]";
            if (peopleList != null && peopleList.get("audience") != null) {
                List<Integer> audience = peopleList.get("audience");
                Set<String> platoons = jedis.zrange(key, 0, -1);
                if (platoons.size() > 0) {
                    Set<String> newPlatoons = new HashSet<>();
                    for (String p : platoons) {
                        Map<String, String> map = JsonUtils.jsonToPojo(p, Map.class);
                        for (int i = 0; i < audience.size(); i++) {
                            String a = audience.get(i) + "";
                            if (a.equals(map.get("uid"))) {
                                newPlatoons.add(p);
                            }
                        }
                    }
                    platoon = newPlatoons.toString();
                }
            }
            return new Response("排麦人列表", platoon, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    /*********************退出房间***********************************/
    //退出房间
    public Response quitRoom(String uid, String roomId) {
        Room room = roomDao.findOneById(roomId);
        Jedis jedis = jedisPool.getResource();
        try {
            if (room != null) {
//            signalingService.login(uid);
                //        减少房间人数
                List<Map<String, Object>> list = JsonUtils.jsonToPojo(jedis.get("room_" + roomId), List.class);
                ResponseSignal signal = new ResponseSignal("100", list);
                this.signalingMsg(roomId, JsonUtils.objectToJson(signal), "00000000");
                deleteRoomPeople(roomId, uid);
                return new Response("退出成功", null, 200);
            } else {
                return new Response("房间不存在", null, 400);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Response("系统出错，请稍后重试",400);
        }finally {
            jedis.close();
        }

//        signalingService.channelLeave(uid);
    }

    /*********************解散房间***********************************/
    //解散房间
    public Response dissolve(String room_id) {
        Jedis jedis = jedisPool.getResource();
        Room room = roomDao.findOneById(room_id);
        try {
            if (room != null) {
                jedis.del("room_" + room_id);
                jedis.del("Counter" + room_id);
                jedis.del("room_people_" + room_id);
                jedis.del("screen_" + room_id);
                jedis.del("roomAdmin_" + room_id + "*");
                String[] roomIds = {room_id};
                roomDao.delete(roomIds);
                signalingService.channelDelAttr(room_id);
                return new Response("解散" + room_id + "房间成功", 200);
            } else {
                return new Response("房间不存在", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", 400);
        } finally {
            jedis.close();
        }
    }

    public List<Room> sort(Room[] arr) {
        Room temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i].getRoom_number() < arr[j].getRoom_number()) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        List<Room> rooms = new ArrayList<>(arr.length);
        for (Room s : arr) {
            rooms.add(s);
        }
        return rooms;
    }

    @Override
    public Response setAdministrator(String uid, String room_id, String duration) {
        //把管理员存入redis,设置过期时间
        Jedis jedis = jedisPool.getResource();
        System.out.println("duration=====>>" + duration);
        System.out.println("uid=====>>" + uid);
        System.out.println("room_id=====>>" + room_id);
        try {
            jedis.set("roomAdmin_" + room_id + uid, uid);
            if (!duration.equals("-1")) {
                jedis.expire("roomAdmin_" + room_id + uid, Integer.valueOf(duration));
            }
            //发送频道消息
            Map<String, Object> map = new HashMap<>();
            map.put("cmd", "301");
            this.signalingMsgOneToOne(JsonUtils.objectToJson(map), uid);
            return new Response("设置成功", null, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    //获取管理员列表
    @Override
    public Response getAdministrators(String roomId) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<String> keys = jedis.keys("roomAdmin_" + roomId + "*");
            List<String> list = new ArrayList<>();
            for (String s : keys) {
                list.add(jedis.get(s));
            }
            List<Map<String, Object>> list2 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                String s = list.get(i);
                User user = userService.findOneById(s);
                map.put("uid", user.getId());
                map.put("name", user.getUsername());
                map.put("avatar", user.getUser_image());
                map.put("duration", jedis.ttl("roomAdmin_" + roomId + s));
                list2.add(i, map);
            }
            return new Response("获取成功", JsonUtils.objectToJson(list2), 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Response removeAdministrators(String roomId, String uid) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del("roomAdmin_" + roomId + uid);
            //发送频道消息
            Map<String, Object> map = new HashMap<>();
            map.put("cmd", "302");
            this.signalingMsgOneToOne(JsonUtils.objectToJson(map), uid);
            return new Response("移除成功", 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Response addAdministratorsDuration(String roomId, String uid, String duration) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long ttl = jedis.ttl("roomAdmin_" + roomId + uid);
            System.out.println("ttl========" + ttl);
            jedis.expire("roomAdmin_" + roomId + uid, (int) (ttl + Integer.valueOf(duration)));
            return new Response("加时成功", null, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        } finally {
            jedis.close();
        }
    }
}