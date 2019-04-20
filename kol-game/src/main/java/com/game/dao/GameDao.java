package com.game.dao;

import com.game.dto.Game;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GameDao {
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    Game findOneById(String id);
    //   添加
    boolean add(Game game);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(Game game);
}
