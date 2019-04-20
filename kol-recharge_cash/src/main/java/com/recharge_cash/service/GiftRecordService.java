package com.recharge_cash.service;

import com.recharge_cash.dto.GiftRecord;
import com.recharge_cash.dao.GiftRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GiftRecordService {
    @Autowired
    GiftRecordDao giftRecordDao;
    public List findByProp(Map map){
        return  giftRecordDao.findByProp(map);
    }
    public GiftRecord findOneById(String id){
        return  giftRecordDao.findOneById(id);
    }
    public boolean add(GiftRecord giftRecord){
        return this.giftRecordDao.add(giftRecord);
    }
    public boolean update(GiftRecord giftRecord){
        return this.giftRecordDao.update(giftRecord);
    }
    public boolean delete(String[] id){
        return giftRecordDao.delete(id);
    }
}
