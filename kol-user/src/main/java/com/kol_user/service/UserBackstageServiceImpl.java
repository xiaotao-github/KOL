package com.kol_user.service;

import com.kol_user.comment.IDUtil;
import com.kol_user.dao.UserBackstageDao;
import com.kol_user.dto.User;
import com.kol_user.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserBackstageServiceImpl implements UserBackstageService {
    @Autowired
    UserBackstageDao userBackstageDao;
    public List findByProp(Map map){
        return  userBackstageDao.findByProp(map);
    }
    public User findOneById(String id){
        return  userBackstageDao.findOneById(id);
    }
    public boolean add(User user){
        String user_id = IDUtil.createAppCode();
        user.setId(user_id);
        return userBackstageDao.add(user);
    }
    public boolean update(User user){
        return userBackstageDao.update(user);
    }
    public boolean delete(String[] id){
        return userBackstageDao.delete(id);
    }

    // @Override
    // public UserRole findUserRoleByName(String username) {
    //     return userBackstageDao.findUserRoleByName(username);
    // }
    //
    // @Override
    // public boolean checkPassword(String username, String password) {
    //     return userBackstageDao.checkPassword(username, password);
    // }
}
