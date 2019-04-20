package com.home.service;

import com.home.dao.IUserDao;
import com.home.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IUserService {
    @Autowired
    IUserDao dao;
    public List findByProp(Map map){
         return dao.findByProp(map);
    };
    public User findByName(String username){
        return dao.findByName(username);
    }
    public User findUserAndRole(String username){
        return dao.findUserAndRole(username);
    }
}
