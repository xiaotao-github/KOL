package com.recharge_cash.dao;

import com.recharge_cash.dto.CoinProportion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CoinProportionDao {
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    CoinProportion findOneById(String id);
    //   添加
    boolean add(CoinProportion coinProportion);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(CoinProportion coinProportion);

}
