package com.coin.dao;

import com.coin.dto.Gift;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GiftDao {
//    后台模块开始
    //    模糊查询
    List findByProp(Map map);
    //    精确查询
    Gift findOneById(String id);
    //   添加
    boolean add(Gift gift);
    //   根据ID删除
    boolean delete(String[] id);
    //    根据ID修改
    boolean update(Gift gift);
//    后台模块结束

    //礼物列表
    List getGiftListToRoom();
}
