package com.kol_room.service;

import com.kol_room.comment.AccessToken;
import com.kol_room.comment.HttpRequest;
import com.kol_room.comment.JsonUtils;
import com.kol_room.dto.DialogueRecord;
import com.kol_room.dto.ResponseSignal;
import com.kol_room.dto.User;
import io.agora.signal.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class SignalingService {
    @Resource
    JedisPool jedisPool;
    Map<String, User> users = new HashMap<>();
    private static String app_id = "479bd1272f9441f5ac59d2750bb817ce";
    private static String app_certificate = "d80b98b8117a4ddabb6dda5e7e40f227";
    List<DialogueRecord> currentAccountDialogueRecords = null;
    private static Long time = new Date().getTime() + 48 * 3600000L;
    //创建Signal 对象 (Signal)
    private Signal sig = new Signal(app_id);
    @Autowired
    private UserRoomService userRoomService;

    public static AccessToken mTokenCreator;


    /**
     * 创建token
     *
     * @param channelName
     * @param uid
     */
    public static String SimpleTokenBuilder(String channelName, String uid) throws Exception {
        mTokenCreator = new AccessToken(app_id, app_certificate, channelName, Integer.valueOf(uid));
        mTokenCreator.addPrivilege(AccessToken.Privileges.kJoinChannel, 0);
        System.out.println("channelName====" + channelName + "；uid====" + uid);
        return buildToken();
    }


    public static String buildToken() throws Exception {
        return mTokenCreator.build();
    }


    public static String getToken(String account) throws NoSuchAlgorithmException {
        StringBuilder digest_String = new StringBuilder().append(account).append(app_id).append(app_certificate).append(time);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(digest_String.toString().getBytes());
        byte[] output = md5.digest();
        String token = hexlify(output);
        String token_String = new StringBuilder().append("1").append(":").append(app_id).append(":").append(time).append(":").append(token).toString();
        return token_String;
    }

    public static String hexlify(byte[] data) {

        char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] toDigits = DIGITS_LOWER;
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return String.valueOf(out);
    }


    //    登录
    public void login(String accountName) {
        try {
            if (users != null && !users.isEmpty()) {
                System.out.println("系统账号已登录");
            } else {
                String token = getToken(accountName);
                System.out.println("token========" + token);
                Signal.LoginSession login = sig.login(accountName, token, new Signal.LoginCallback() {
                    public void onLoginSuccess(Signal.LoginSession session, int uid) {
                        System.out.println(accountName + "登录成功");
                    }

                    public void onLogout(Signal.LoginSession session, int ecode) {
                        System.out.println("退出登录");
                    }

                    public void onLoginFailed(Signal.LoginSession session, int ecode) {
                        System.out.println(accountName + "登录错误");
                    }
                });
                User user = new User(login, accountName);
                users.put(accountName, user);
                System.out.println(login);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        String str = "fdfdsfsfdsfds";
        String str1 = str.substring(0, str.length() -1);
        System.out.print(str1);
    }*/

    //    加入频道
    public void joinChannel(String channelName, String accountName) {
        Jedis jedis = jedisPool.getResource();
        try {
            if (users != null && !users.isEmpty()) {
                System.out.println("系统账号已登录");
            } else {
                System.out.println("进来了了了了了了了了");
                this.login(accountName);
            }
            Signal.LoginSession.Channel channel = users.get(accountName).getSession().channelJoin(channelName, new Signal.ChannelCallback() {

                public void onChannelJoinFailed(Signal.LoginSession session, Signal.LoginSession.Channel channel, int ecode) {
                    System.out.println(accountName + "加入频道失败!");
                }

                public void onChannelLeaved(Signal.LoginSession session, Signal.LoginSession.Channel channel, int ecode) {
                    System.out.println(accountName + "用户离开频道");
                }

                public void onChannelUserJoined(Signal.LoginSession session, Signal.LoginSession.Channel channel, String account, int uid) {

                    System.out.println("其他用户" + account + "加入频道");
                }

                public void onChannelUserLeaved(Signal.LoginSession session, Signal.LoginSession.Channel channel, String account, int uid) {
                    //清除account在channelName房间的麦位信息
                    List<Map<String, Object>> microphones = JsonUtils.jsonToPojo(jedis.get("room_" + channelName), List.class);
                    if (microphones != null) {
                        for (int i = 0; i < microphones.size(); i++) {
                            if (microphones.get(i) != null && microphones.get(i).get("user") != null && ((Map) microphones.get(i).get("user")).get("uid").equals(account)) {
                                microphones.set(i, new HashMap<>());
                                ResponseSignal responseSignal = new ResponseSignal("100", microphones);
                                String msg = JsonUtils.objectToJson(responseSignal);
                                userRoomService.signalingMsg(channelName, msg, "00000000");
                                //离开频道
                                channelLeave(channelName);
                                //        减少房间人数
                                userRoomService.deleteRoomPeople(channelName, account);
                                // 查看当前房间是否有人在排麦
                                Set<String> platoon = jedis.zrange("platoon" + channelName, 0, 0);
                                if (platoon.size() > 0) {
                                    //        如果有人在排麦，就用信道给那个排麦第一名的人发点对点消息
                                    Map map = JsonUtils.jsonToPojo(platoon.iterator().next(), Map.class);
                                    String user_id1 = (String) map.get("user_id");
                                    Map<Object, Object> map1 = new HashMap<>();
                                    map1.put("cmd", "102");
                                    //利用信道去通知
                                    userRoomService.signalingMsgOneToOne(JsonUtils.objectToJson(map1), user_id1);
                                }
                                jedis.set("room_" + channelName, JsonUtils.objectToJson(microphones));
                            }
                        }
                    }
                    System.out.println("其他用户" + account + "离开频道" + channelName);
                }

                public void onChannelUserList(Signal.LoginSession session, Signal.LoginSession.Channel channel, List<String> users, List<Integer> uids) {
                    System.out.println("频道用户列表" + users.size());
                }

                public void onMessageChannelReceive(Signal.LoginSession session, Signal.LoginSession.Channel channel, String account, int uid, String msg) {
                    System.out.println("收到频道消息" + msg);
                }

                public void onChannelQueryUserNumResult(Signal.LoginSession session, String err, int num) {
                    System.out.println("频道用户人数" + num);
                }
            });
            users.get(accountName).setChannel(channel);
            users.put(accountName, users.get(accountName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        String name = users.get(accountName).getChannel().getName();
        System.out.println(accountName + "加入频道" + name);
    }

    //    发送频道消息
    public void channelDeal(String command, String accountName) {
        try {
            users.get(accountName).getChannel().messageChannelSend(command, new Signal.MessageCallback(){
                public void onMessageSendSuccess(Signal.LoginSession session) {
                    System.out.println("发送频道消息成功");
                }

                public void onMessageSendError(Signal.LoginSession session, int ecode) {
                    System.out.println("发送频道消息失败");
                }
            });
            System.out.println(accountName + "发送频道消息!!!!!!");
            users.get(accountName).getChannel().channelLeave();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //一对一发送消息
    //    oppositeAccount为用户登录厂商 app 的账号
    public void sendMsg(final String msg, String oppositeAccount) {
        try {
            User user = users.get("00000000");
            if (user != null) {
                System.out.println("系统账号已登录");
            } else {
                login("00000000");
            }
            Signal.LoginSession currentSession = users.get("00000000").getSession();
            currentSession.messageInstantSend(oppositeAccount, msg, new Signal.MessageCallback() {
                @Override
                public void onMessageSendSuccess(Signal.LoginSession session) {
                    DialogueRecord dialogueRecord = new DialogueRecord(oppositeAccount, msg, new Date());
                    currentAccountDialogueRecords.add(dialogueRecord);
                    System.out.println("一对一发送消息-成功");
                }

                @Override
                public void onMessageSendError(Signal.LoginSession session, int ecode) {
                    System.out.println("一对一发送消息-失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询用户在房间状态
    public boolean getStatus(String cname, String uid) {
        //http://api.agora.io/dev/v1/channel/user/property/479bd1272f9441f5ac59d2750bb817ce/29128309/20190216
        String agoraUrl = "https://api.agora.io/dev/v1/channel/user/property/";
        String agoraResult = HttpRequest.sendGet(agoraUrl, app_id + "/" + uid + "/" + cname);
        return (boolean) ((Map) JsonUtils.jsonToPojo(agoraResult, Map.class).get("data")).get("in_channel");
    }

    //查询房间在线人员列表
    public Map<String, List<Integer>> getPeopleList(String cname) {
        //http://api.agora.io/dev/v1/channel/user/479bd1272f9441f5ac59d2750bb817ce/87445904
        String agoraUrl = "https://api.agora.io/dev/v1/channel/user/";
        String agoraResult = HttpRequest.sendGet(agoraUrl, app_id + "/" + cname);
        boolean o = (boolean) ((Map) JsonUtils.jsonToPojo(agoraResult, Map.class).get("data")).get("channel_exist");
        System.out.println(agoraResult);
        System.out.println("是否有人" + o);
        if (o) {
            //观众列表
            List<Integer> list1 = (List<Integer>) ((Map) JsonUtils.jsonToPojo(agoraResult, Map.class).get("data")).get("audience");
            //主播(麦位上的人)列表
            List<Integer> list2 = (List<Integer>) ((Map) JsonUtils.jsonToPojo(agoraResult, Map.class).get("data")).get("broadcasters");
            Map<String, List<Integer>> map = new HashMap<>();
            map.put("audience", list1);
            map.put("broadcasters", list2);
            if (list1 != null && list1.size() > 0) {
                System.out.println(list1.get(0));
            }
            return map;
        }
        return null;
    }

    //退出房间（离开频道）
    public void channelLeave(String uid) {
        try {
            users.get(uid).getChannel().channelLeave();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解散房间（删除频道）
    public void channelDelAttr(String channelName) {
        try {
            users.get("00000000").getChannel().channelDelAttr(channelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //退出登录
    public void logout(String account) {
        try {
            String token = getToken(account);
            System.out.println("token========" + token);
            sig.login(account + 1, token, new Signal.LoginCallback()).logout();
            System.out.println(account + "退出登录");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
