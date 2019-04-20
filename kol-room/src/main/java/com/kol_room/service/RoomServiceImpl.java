package com.kol_room.service;
import com.kol_room.dao.RoomDao;
import com.kol_room.dto.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    RoomDao roomDao;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    UserRoomServiceImpl userRoomService;
    public List findByProp(Map map){
        Jedis jedis=jedisPool.getResource();
        try {
            List resultList=new ArrayList();
            List list=roomDao.findByProp(map);
            for (int i=0;i<list.size();i++){
                Room room=(Room) list.get(i);
                String room_id=room.getId();
                Long room_number=userRoomService.showRoomPeopleNum(room_id);
                room.setRoom_number(room_number);
                resultList.add(room);
            }
            return resultList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            jedis.close();
        }
    }
    public Room findOneById(String id){
        return  roomDao.findOneById(id);
    }
//    创建房间
    public boolean add(Room room){
        return roomDao.add(room);
    }
    public boolean update(Room room){
        return roomDao.update(room);
    }
    public boolean delete(String[] id){
        return roomDao.delete(id);
    }
}
