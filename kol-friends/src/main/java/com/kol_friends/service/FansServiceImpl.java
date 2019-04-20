package com.kol_friends.service;

import com.kol_friends.dao.FansDao;
import com.kol_friends.dto.Fans;
import com.kol_friends.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FansServiceImpl implements FansService {
    @Autowired
    FansDao fansDao;
    @Autowired
    FocusService focusService;

    @Override
    public List getFansList(String user_id) {
        List list = fansDao.getFansList(user_id);
        return list;
    }

    @Override
    public Fans getOnlyFans(String user_id, String fans_id) {
        Fans fans = fansDao.getOnlyFans(user_id,fans_id);
        return fans;
    }


    /**
     * c测试
     * @param user_id
     * @param keyword
     * @return
     */
    @Override
    public List getFansListByWord(String user_id, String keyword) {
        List fansList = fansDao.getFansListByWord(user_id, keyword);
        List focusList = focusService.getFocusList(user_id);
        List list = new ArrayList();
        Set focus = new HashSet(focusList);
        for(int i=0;i<fansList.size();i++){
            Fans fan= (Fans) fansList.get(i);
            String fan_id=fan.getFans_id();
            Map map=new HashMap();
            Map map1=new HashMap();
            if (focus.contains(fan_id)){
                System.out.println("互为关注");
                User user = fansDao.getUserMessage(fan_id);
                map.put("user",user);
                map.put("isFollow","1");
                //替换fansList的值
                // fansList.set(i, map);
                list.add(i,map);
            }else {
                System.out.println("没有关注");
                User user = fansDao.getUserMessage(fan_id);
                map1.put("user",user);
                map1.put("isFollow","0");
                // fansList.set(i,map1);
                list.add(i,map1);
           }
        }
        // return fansList;
        return list;
    }

    @Override
    public void add(Fans fans) {
        fansDao.add(fans);
    }

    @Override
    public void addFans(String user_id, String fans_id) {
        Fans fans = fansDao.getOnlyFans(user_id, fans_id);
        if (fans==null){
            Fans fans1 = new Fans();
            fans1.setUser_id(user_id);
            fans1.setFans_id(fans_id);
            fans1.setId("");
            fansDao.add(fans1);
        }else {
            return;
        }
    }

    @Override
    public void cancelFans(String user_id, String fans_id) {
        fansDao.cancelFans(user_id, fans_id);
    }

    /**
     * 获取粉丝数量
     * @return
     */
    @Override
    public String getFansNum(String user_id) {
        List list = fansDao.getFansList(user_id);
        return String.valueOf(list.size());
    }
}
