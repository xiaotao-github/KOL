package com.coin.service;


import com.coin.dto.Gift;

import java.util.List;
import java.util.Map;

public interface GiftService {

    public List findByProp(Map map);
    public Gift findOneById(String id);
    public boolean add(Gift gift);
    public boolean update(Gift gift);
    public boolean delete(String[] id);
    //    礼物列表
    public List getGiftListToRoom();
}
