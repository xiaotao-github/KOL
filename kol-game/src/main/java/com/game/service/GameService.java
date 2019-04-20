package com.game.service;

import com.game.dao.GameDao;
import com.game.dto.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameService {
    @Autowired
    GameDao gameDao;
    public List findByProp(Map map){
        return  gameDao.findByProp(map);
    }
    public Game findOneById(String id){
        return  gameDao.findOneById(id);
    }
    public boolean add(Game game){
        return gameDao.add(game);
    }
    public boolean update(Game game){
        return gameDao.update(game);
    }
    public boolean delete(String[] id){
        return gameDao.delete(id);
    }
}
