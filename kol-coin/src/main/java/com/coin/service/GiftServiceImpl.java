package com.coin.service;

import com.coin.comment.JsonUtils;
import com.coin.dao.GiftDao;
import com.coin.dto.Gift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiftServiceImpl implements GiftService {
    @Autowired
    GiftDao giftDao;
    @Override
    public List findByProp(Map map) {
        return giftDao.findByProp(map);
    }

    @Override
    public Gift findOneById(String id) {
        return giftDao.findOneById(id);
    }

    @Override
    public boolean add(Gift gift) {
        return this.giftDao.add(gift);
    }

    @Override
    public boolean update(Gift gift) {
        return this.giftDao.update(gift);
    }

    @Override
    public boolean delete(String[] id) {
        return giftDao.delete(id);
    }

    //礼物列表
    @Override
    public List getGiftListToRoom() {
        List giftListToRoom = giftDao.getGiftListToRoom();
        return giftListToRoom;
    }
}
