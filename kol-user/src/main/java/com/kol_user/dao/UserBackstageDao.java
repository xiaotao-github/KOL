package com.kol_user.dao;

import com.kol_user.dto.User;
import com.kol_user.dto.UserRole;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserBackstageDao extends Dao<User> {
    //查询后台用户角色的信息
    UserRole findUserRoleByName(String username);
    //校验用户名密码是否正确
    boolean checkPassword(String username,String password);
}
