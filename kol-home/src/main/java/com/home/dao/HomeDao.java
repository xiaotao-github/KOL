package com.home.dao;

import com.home.dto.Home;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HomeDao {
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    Home findOneById(String id);
    //   添加
    boolean add(Home home);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(Home home);

    List flashView();

}
