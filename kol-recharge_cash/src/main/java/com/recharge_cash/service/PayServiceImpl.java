package com.recharge_cash.service;


import com.recharge_cash.comment.WxMD5Util;
import com.recharge_cash.dao.RechargeOrderDao;
import com.recharge_cash.dto.RechargeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    WXPayOrderService wxPayOrderService;
    @Autowired
    AliPayOrderService aliPayOrderService;
    @Autowired
    CoinRechargeService coinRechargeService;
    @Autowired
    RechargeOrderDao rechargeOrderDao;

    //支付宝订单
    @Override
    public String getOrderByAliPay(String rechargeId,String user_id) {
        String total_fee = String.valueOf(coinRechargeService.coinRechargeById(rechargeId));
        String aliPayOrderStr = aliPayOrderService.getAliPayOrderStr(total_fee, user_id);
        return aliPayOrderStr;
    }

    //微信订单
    @Override
    public Map<String, String> getOrderByWXPay(String rechargeId, String user_id) {
        // Integer total_fee =Integer.parseInt(new java.text.DecimalFormat("0").format(coinRechargeService.coinRechargeById(rechargeId)*100));
        // Double value = Double.valueOf(coinRechargeService.coinRechargeById(rechargeId)/100);
        int total_fee = coinRechargeService.coinRechargeById(rechargeId);
        Map<String, String> result = null;
        try {
            //请求预支付订单prepay_id
            result = wxPayOrderService.getUnifiedOrder(total_fee, user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();

        //返回APP端的数据
        //参加调起支付的签名字段有且只能是6个，分别为appid、partnerid、prepayid、package、noncestr和timestamp，而且都必须是小写
        map.put("appid", result.get("appid"));
        map.put("partnerid", result.get("mch_id"));
        map.put("prepayid", result.get("prepay_id"));
        //扩展字段,暂填写固定值Sign=WXPay
        map.put("package", "Sign=WXPay");
        //随机字符串
        map.put("noncestr", result.get("nonce_str"));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));//单位为秒
//      这里不要使用请求预支付订单时返回的签名（新建一个sign）
        WxMD5Util md5Util = new WxMD5Util();
        try {
            String sign = md5Util.getSign(map);
            System.out.println(sign);
            map.put("sign",sign );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //订单查询
    @Override
    public byte orderInquiry(String order_number) {
        RechargeOrder rechargeOrder = rechargeOrderDao.selectBytransactionId(order_number);
        if (rechargeOrder!=null && !rechargeOrder.getOrder_number().equals("")){
            return rechargeOrder.getState();
        }else {
            return -2;
        }
    }
}
