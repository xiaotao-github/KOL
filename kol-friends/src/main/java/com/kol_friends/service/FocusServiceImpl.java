package com.kol_friends.service;

import com.kol_friends.dao.FocusDao;
import com.kol_friends.dto.Focus;
import com.kol_friends.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FocusServiceImpl implements FocusService {

    @Autowired
    FocusDao focusDao;
    //获取关注列表
    @Override
    public List getFocusList(String user_id) {
        List list = focusDao.getFocusList(user_id);
        return list;
    }

    //获取唯一关注对象
    @Override
    public Focus getOnlyFocus(String user_id, String focus_id) {
        Focus focus = focusDao.getOnlyFocus(user_id, focus_id);
        return focus;
    }

    @Override
    public void add(Focus focus) {
        focusDao.add(focus);
    }

    //添加关注
    @Override
    public void addFocus(String user_id, String focus_id) {
        Focus focus = focusDao.getOnlyFocus(user_id, focus_id);
        if (focus==null){
            Focus focus1 = new Focus();
            focus1.setUser_id(user_id);
            focus1.setFocus_id(focus_id);
            //后台已设置ID触发器
            focus1.setId("");
            focusDao.add(focus1);
        }else {
            return;
        }
    }

    //取消关注
    @Override
    public void cencelFocus(String user_id, String focus_id) {
        focusDao.cencelFocus(user_id, focus_id);
    }

    //查询结果列表
    @Override
    public List getFocusListByWord(String user_id, String keyword) {
        List list = focusDao.getFocusListByWord(user_id, keyword);
        for (int i = 0;i<list.size();i++){
            Map map = new HashMap();
            Focus focus= (Focus) list.get(i);
            String focus_id=focus.getFocus_id();
            User user = focusDao.getUserMessage(focus_id);
            map.put("user",user);
            list.set(i,map);
            // list.set(i,user);
        }
        return list;
    }

    @Override
    public List addFocusListByWord(String keyword) {
        List list = focusDao.addFocusListByWord(keyword);
        return list;
    }

    @Override
    public String getFocusNum(String user_id) {
        List list = focusDao.getFocusList(user_id);
        return String.valueOf(list.size());
    }
}
