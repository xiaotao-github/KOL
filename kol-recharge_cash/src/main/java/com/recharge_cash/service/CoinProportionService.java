package com.recharge_cash.service;

import com.recharge_cash.dto.CoinProportion;
import com.recharge_cash.dao.CoinProportionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CoinProportionService {
    @Autowired
    CoinProportionDao coinProportionDao;
    public List findByProp(Map map){
        return  coinProportionDao.findByProp(map);
    }
    public CoinProportion findOneById(String id){return  coinProportionDao.findOneById(id); }
    public boolean add(CoinProportion coinProportion){
        return coinProportionDao.add(coinProportion);
    }
    public boolean update(CoinProportion coinProportion){
        return coinProportionDao.update(coinProportion);
    }
    public boolean delete(String[] id){
        return coinProportionDao.delete(id);
    }
}
