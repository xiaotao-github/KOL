package com.recharge_cash.comment;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GetOrderNoUtil {
    public static String getOrderIdByTimeAndUserId(String user_id) {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // String newDate = sdf.format(new Date());
        String newDate = String.valueOf(System.currentTimeMillis() / 1000);
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
        result += random.nextInt(10);
        }
        return newDate + result+user_id;
    }
}
