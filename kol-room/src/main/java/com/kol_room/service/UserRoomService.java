package com.kol_room.service;


import com.coin.dto.GiftOrder;
import com.kol_room.dto.Response;
import com.kol_room.dto.UserDetail;
import com.kol_user.dto.User;

import java.util.List;
import java.util.Map;

public interface UserRoomService {
    void signalingMsg(String channelName, String msg, String account);

    void signalingMsgOneToOne(String msg, String user_id);
    //禁麦
    Response prohibit_microphones(String room_id, int position);

    //    封麦
    Response ban_microphones(String room_id, int position);


    Response platoonList(String room_id);
    //排麦
    Response platoon(Map map, String room_id);

    // Map<String, String> searchUserAndRoom(String words);

    Response giveGift(List<GiftOrder> giftOrders, String room_id, String gift_id, String giver_id, String[] acceptIds);
    //解散房间
    Response dissolve(String user_id);
    //获取用户信息
    User getOneUser(String user_id);
    //    创建房间
    Response createRoom(String user_id, String room_label, String limit, String room_name, String room_topic);
    //    得到在麦位上的用户头像和名字
    UserDetail getUser(String user_id, int position);
    //    上麦,需要传入用户id，麦位，房间id，要通过信道通知其他用户
    Response up_phone(String user_id, int position, String room_id, String channelName);
    //下麦，要通过信道通知其他用户
    Response down_phone(String room_id, String user_id);
    //    显示麦位信息
    List show_microphone(String room_id) throws ClassNotFoundException ;
    //退出房间
////    position表示麦位，如果要退出房间的用户不在麦位上，那么传输一个-1
    Response quitRoom(String uid,String roomId);
    //    显示房间所有人
    Long showRoomPeopleNum(String room_id);
    //    房间在线人列表
    Response showRoomPeopleList(String room_id);
    //  房间人数新增
    void addRoomPeople(String room_id, String user_id);
    //    房间人数减少
    void deleteRoomPeople(String room_id, String user_id);
    //踢出房间
    Response getOutRoom(String cname, String uid, String room_id);
    //    用户的房间列表
    Response selectRoomList(int label, int pageNo);
    //    修改房间设置信息
    boolean updateRoom(Map map);
    //    根据房间id来查找房间信息
    Response findOneById(String id, String user_id);
    //    打开|关闭 计数器
    Response Counter(String roomId);

    void request_down_phone(String roomId, int position, String uid);

    //    进入房间
    Response joinRoom(String room_id, String user_id);

    Response setAdministrator(String uid, String room_id, String duration);

    Response getAdministrators(String roomId);

    Response removeAdministrators(String roomId, String uid);

    Response addAdministratorsDuration(String roomId, String uid, String duration);

    void invite(String user_id);

    Map<String, Object> preInfo(String roomId, String user_id);

    List searchRoomByKeyWord(String keyWord);

    Response Screen(String roomId);
}
