package com.coin.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

//排行榜表
@Mapper
public interface RankingListDao {
    //    精确查询
    Map<String,Object> selectOneByUid(String uid);

    //   添加
    boolean insert(Map<String,Object> rankingList);

    //   根据ID删除
    boolean delete(String uid);

    //    根据ID修改
    boolean update(Map<String,Object> rankingList);
}
