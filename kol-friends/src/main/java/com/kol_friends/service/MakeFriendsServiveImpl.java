package com.kol_friends.service;


import com.kol_friends.dao.MakeFriendsDao;
import com.kol_friends.dto.MakeFriends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakeFriendsServiveImpl implements MakeFriendsService {
    @Autowired
    MakeFriendsDao makeFriendsDao;

    private static final int intimacyValue = 5;

    /***
     * 好友列表ID
     * @param user_id
     * @return
     */
    public List getFriendsList(String user_id) {
        List list = makeFriendsDao.getFriendsList(user_id);
        return list;
    }

    public void add(MakeFriends makeFriends) {
        makeFriendsDao.add(makeFriends);
    }

    /***
     * 添加好友
     * @param user_id
     * @param friends_id
     */
    public void addFriends(String user_id, String friends_id) {
        MakeFriends makeFriends = new MakeFriends();
        makeFriends.setUser_id(user_id);
        makeFriends.setFriends_id(friends_id);
        makeFriendsDao.add(makeFriends);
        MakeFriends makeFriends1 = new MakeFriends();
        makeFriends1.setUser_id(friends_id);
        makeFriends1.setFriends_id(user_id);
        makeFriendsDao.add(makeFriends1);
    }

    /***
     * 增加亲密度
     * @param user_id
     * @param friends_id
     */
    public void addIntimacy(String user_id, String friends_id) {
        MakeFriends makeFriends = makeFriendsDao.getOnlyFriends(user_id, friends_id);
        MakeFriends makeFriends1 = makeFriendsDao.getOnlyFriends(friends_id, user_id);
        //需要异常处理，不能直接返回
        if (makeFriends==null||makeFriends1==null){
            return;
        }
        // else {
        //     if (friends.getIntimacy_value()+intimacyValue>=100){
        //         friends.setIntimacy_value(100);
        //         friends1.setIntimacy_value(100);
        //         friendsDao.update(friends);
        //         friendsDao.update(friends1);
        //     }else {
        //         friends.setIntimacy_value(friends.getIntimacy_value()+intimacyValue);
        //         friends1.setIntimacy_value(friends1.getIntimacy_value()+intimacyValue);
        //         friendsDao.update(friends);
        //         friendsDao.update(friends1);
        //     }
        // }

    }

    public void update(MakeFriends makeFriends) {
        makeFriendsDao.update(makeFriends);
    }
}
