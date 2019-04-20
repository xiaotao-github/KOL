package com.home.service;

import com.home.dao.HomeDao;
import com.home.dto.FlashView;
import com.home.dto.Home;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HomeService {
    @Autowired
    HomeDao homeDao;
    public List findByProp(Map map){
        return  homeDao.findByProp(map);
    }
    public Home findOneById(String id){
        return  homeDao.findOneById(id);
    }
    public boolean add(Home home){
        return homeDao.add(home);
    }
    public boolean update(Home home){
        return homeDao.update(home);
    }
    public boolean delete(String[] id){
        return homeDao.delete(id);
    }

    public List flashView(){
        List list = homeDao.flashView();
        for (int i = 0;i<list.size();i++){
            FlashView flashView = (FlashView) list.get(i);
            flashView.setImageUrl("http://129.204.52.213/home_image/"+flashView.getImageUrl());
            list.set(i,flashView);
        }
        return list;
    }
}
