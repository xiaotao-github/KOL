package com.recharge_cash.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.recharge_cash.comment.JsonUtils;
import com.recharge_cash.comment.TokenUtil;
import com.recharge_cash.dto.Response;
import com.recharge_cash.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/Pay")
public class PayController {
    @Autowired
    PayService payService;
    @Autowired
    WXPayOrderService wxPayOrderService;
    @Autowired
    AliPayOrderService aliPayOrderService;
    @Autowired
    CoinRechargeService coinRechargeService;


    private final static Logger logger= LoggerFactory.getLogger(PayController.class);


    //金币充值界面列表（X币兑换比例）
    //注意请求头,如何是text/json会出现406错误
    @RequestMapping(value = "/coinRecharge", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Response coinRecharge(HttpServletRequest request){
        Response response;
        try {
            response = new Response("金币充值界面列表",coinRechargeService.coinRecharge(), 200);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统异常，请稍后重试", null, 400);
        }
        return response;
    }


    //获取充值订单
    @RequestMapping(value = "/getOrder", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Response getOrder(HttpServletRequest request, String rechargeId, String channel){
        Response response;
        try {
            DecodedJWT jwt = TokenUtil.parseJWT(request.getHeader("token"));
            //从token中获取user_id
            String user_id = jwt.getClaim("user_id").asString();
            String channel1 = channel.trim();
            if (!channel1.equals("")&&channel1.equals("wx")){
                Map<String, String> orderByWXPay = payService.getOrderByWXPay(rechargeId, user_id);
                response = new Response("微信订单生成成功", JsonUtils.objectToJson(orderByWXPay),200);
            }else if (!channel1.equals("")&&channel1.equals("alipay")){
                String orderByaALiPay = payService.getOrderByAliPay(rechargeId,user_id);
                response = new Response("支付宝订单生成成功", orderByaALiPay,200);
            }else {
                response = new Response("参数传输错误",null,400);
            }
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }


    /**
     *   微信支付回调
     *   支付异步结果通知，我们在请求预支付订单时传入的地址
     *   官方文档 ：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3
     */
    @RequestMapping(value = "/wxPayNotify", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxPayOrderService.payBack(resXml);
            return result;
        } catch (Exception e) {
            System.out.println("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

    /*
     * 支付宝支付成功后，异步请求该接口
     * @param request
     * @return
     * @throws IOException
     * */
    @RequestMapping(value = "/aliPayNotify_url",method = RequestMethod.POST)
    @ResponseBody
    public String notify(HttpServletRequest request, HttpServletResponse response)throws IOException{
        System.out.println("支付宝异步返回开始");
        logger.info("支付宝异步返回支付结果开始");
//        1、从支付宝回调的request域中取值
//        获取支付宝返回的参数集合
        Map<String,String[]> aliParams=request.getParameterMap();
//        用以存放转化后的参数集合
        Map<String,String> conversionParams=new HashMap<String,String>();
        for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();){
            String key=iter.next();
            String[] values=aliParams.get(key);
            String valueStr="";
            for (int i=0;i<values.length;i++){
                valueStr=(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
            }
//            乱码解决，这段代码在出现乱码的时候使用，如果mysign和sign不相等也可以使用这段代码转化
            //valueStr=new String(valueStr.getBytes("ISO-8859-1"),"utf-8");
            conversionParams.put(key,valueStr);
        }
        logger.info("返回参数集合："+conversionParams);
        System.out.println("返回参数集合"+conversionParams);
        return aliPayOrderService.notify(conversionParams);
    }


    /****
     * 微信订单查询
     * @param transaction_id
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderInquiryByWX")
    @ResponseBody
    public Map<String,String> orderInquiryByWX(String transaction_id) throws Exception {
        Map<String,String> resultMap = wxPayOrderService.orderInquiry(transaction_id);
        //返回支付结果
        return resultMap;
    }

    //支付宝发起了订单查询请求
    @RequestMapping("/orderInquiryByAli")
    @ResponseBody
    public byte orderInquiryByAli(String outTradeNo) throws Exception {
        Byte byte1 = aliPayOrderService.checkAlipay(outTradeNo);
        //返回支付结果
        return byte1;
    }

    //订单查询
    @RequestMapping("/orderInquiry")
    @ResponseBody
    public Response orderInquiry(String order_number){
        Response response;
        try {
            byte b = payService.orderInquiry(order_number);
            if (b!=-2){
                response = new Response("查询订单成功",String.valueOf(b),200);
            }else {
                response = new Response("订单不存在",null,400);
            }
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试",null,400);
        }
        return response;
    }
}
