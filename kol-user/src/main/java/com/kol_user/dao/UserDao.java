package com.kol_user.dao;

import com.kol_user.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserDao extends Dao<User> {
    //根据电话号码查询
    User findOneByTelephone(String telephone);
    List findByProp(Map map);

    User findOneByIdentityId(String identityId);


    //返回房间信息
    Room findRoomById(String roomId);

    //多用户查询
    List findInfosByListId(String[] id);

    List searchUserByKeyWord(String keyWord);

    //新增用户
    boolean addUser(UserCoins userCoins);

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
