package com.home.dao;

import com.home.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserDao extends HomeDao{
    User findUserAndRole(String username);
    User findByName(String username);
}
