package com.kol_user.dao;

import com.kol_user.dto.User;

import java.util.List;
import java.util.Map;

public interface Dao<T> {
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    T findOneById(String id);
    //   添加
    boolean add(User user);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(User user);
}
