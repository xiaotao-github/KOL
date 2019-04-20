package com.kol_room.dao;

import com.kol_room.dto.Room;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomDao {
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    Room findOneById(String id);
    //   添加
    boolean add(Room room);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(Room room);

    //    创建房间
    boolean createRoom(Room room);
    List selectRoomListByOffic();
    List selectRoomListByNormal();
    boolean updateRoom(Map map);
    List selectRoomList(int label);

    List selectRoomAllList();

    //管理员设置
    void addAdmin(String room_id, String user_id);
    void cancelAdmin(String room_id, String user_id);

    List searchRoomByKeyWord(String keyWord);
}
