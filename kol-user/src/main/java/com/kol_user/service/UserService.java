package com.kol_user.service;

import com.kol_user.dto.Remark;
import com.kol_user.dto.ReportInformation;
import com.kol_user.dto.Room;
import com.kol_user.dto.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findOneById(String id);
    User findOneByTelephone(String telephone);
    List findByProp(Map map);
    List topAdvertisement();
    boolean updateImformation(User user);
    void updateTelephone(String id, String telephone);
    void addTelephone(String telephone, String password);
    void add(User user);
    boolean checkTelephone(String telephone);
    // String showUserPage(String user_id);

    String forgetPassword(String telephone,String rdNum,String password);
    String visitUserPage(String user_id, String targetUser_id);

    User findOneByIdentityId(String identityId);

    Room findRoomById(String roomId);
    //多用户查询，返回用户基本信息
    List findInfosByListId(String[] id);
    //判断性别是否被修改
    Integer checkSexTime(String id);

    // //判断是否接收到新礼物
    // void isAcceptNewGift(String id);
    //获取用户基本信息
    String getUserInfos(String id);

    List searchUserByKeyWord(String keyWord);

    //获取用户个人资料
    String getUserInfosByUser(String user_id,String uid);

    //新增举报信息
    boolean addReportInformation(ReportInformation reportInformation);

    //新增用户备注
    boolean addRemark(Remark remark);
    //查询用户备注名称
    Remark searchUserRemark(String user_id,String uid);
    //修改用户备注
    boolean updateRemark(Remark remark);

    //用户修改昵称后更新房间模块的用户昵称
    boolean updateRoomUsername(Room room);
}
