package com.recharge_cash.service;

import com.recharge_cash.dto.Recharge;
import com.recharge_cash.dao.RechargeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RechargeService {
    @Autowired
    RechargeDao rechargeDao;
    public List findByProp(Map map){
        return  rechargeDao.findByProp(map);
    }
    public Recharge findOneById(String id){
        return  rechargeDao.findOneById(id);
    }
    public boolean add(Recharge recharge){
        return this.rechargeDao.add(recharge);
    }
    public boolean update(Recharge recharge){
        return this.rechargeDao.update(recharge);
    }
    public boolean delete(String[] id){
        return rechargeDao.delete(id);
    }
}
