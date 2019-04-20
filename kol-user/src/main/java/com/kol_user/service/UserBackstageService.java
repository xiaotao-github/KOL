package com.kol_user.service;

import com.kol_user.dto.User;
import com.kol_user.dto.UserRole;

import java.util.List;
import java.util.Map;

public interface UserBackstageService {
    List findByProp(Map map);
    User findOneById(String id);
    boolean add(User user);
    boolean update(User user);
    boolean delete(String[] id);

    //
    // //后台角色管理
    // UserRole findUserRoleByName(String username);
    // //校验用户名密码是否正确
    // boolean checkPassword(String username,String password);
}
