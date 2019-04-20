package com.recharge_cash.dao;

import com.recharge_cash.dto.Recharge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RechargeDao {
    //后台模块开始

    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    Recharge findOneById(String id);
    //   添加
    boolean add(Recharge recharge);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(Recharge recharge);

    //后台模块结束
}
