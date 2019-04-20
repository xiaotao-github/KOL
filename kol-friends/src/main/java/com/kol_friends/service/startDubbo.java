package com.kol_friends.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class startDubbo {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:config/*.xml");
        context.start();
        System.out.println("启动成功");
//        logger.info("dubbo service begin to start");
        try {
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
