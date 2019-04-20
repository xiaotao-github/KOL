package com.kol_room.service;



import com.kol_room.dto.Room;

import java.util.List;
import java.util.Map;

public interface RoomService {
    public List findByProp(Map map);
    public Room findOneById(String id);
    //    创建房间
    public boolean add(Room room);
    public boolean update(Room room);
    public boolean delete(String[] id);
}
