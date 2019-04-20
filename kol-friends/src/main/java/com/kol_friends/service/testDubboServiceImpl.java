package com.kol_friends.service;

import org.springframework.stereotype.Service;

@Service
public class testDubboServiceImpl implements testDubboService {
    public void testDubbo(String words){
        System.out.println("dubbo成功了吧"+words);
    }
}
