package com.recharge_cash.service;


import com.coin.dto.UserCoins;
import com.coin.service.GiftOrderService;
import com.coin.service.UserCoinsService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.kol_user.dto.User;
import com.kol_user.service.UserService;
import com.recharge_cash.comment.GetOrderNoUtil;
import com.recharge_cash.comment.WXpayConfig;
import com.recharge_cash.comment.WxMD5Util;
import com.recharge_cash.dao.RechargeOrderDao;
import com.recharge_cash.dto.RechargeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayOrderServiceImpl implements WXPayOrderService {
    @Autowired
    RechargeOrderDao rechargeOrderDao;
    @Autowired
    CoinRechargeService coinRechargeService;
    @Resource(name = "giftOrderServiceImpl")
    GiftOrderService giftOrderService;
    @Resource(name = "userServiceImpl")
    UserService userService;
    @Resource(name = "userCoinsServiceImpl")
    UserCoinsService userCoinsService;

    //终端IP，调用微信支付API的机器IP
    public static final String SPBILL_CREATE_IP = "129.204.52.213";
    // public static final String SPBILL_CREATE_IP = "192.168.12.93";
    //通知地址，接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    // public static final String NOTIFY_URL = "http://你的域名/v1/weixin/wxPayNotify.json";
    public static final String NOTIFY_URL = "http://129.204.52.213:8081/kol_recharge_cash/Pay/wxPayNotify";
    //支付类型APP
    public static final String TRADE_TYPE_APP = "APP";

    private final static Logger logger= LoggerFactory.getLogger(WXPayOrderServiceImpl.class);


    /***
     * 调用官方SDK 获取预支付订单等参数
     // * @param attach 附加数据
     * @param total_fee 支付金额
     * @param user_id  用户id
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,String> getUnifiedOrder(int total_fee,String user_id) throws Exception {
        WxMD5Util md5Util = new WxMD5Util();
        Map<String, String> returnMap = new HashMap<>();
        WXpayConfig config = new WXpayConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();

        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        //随机字符串（官方获取随机数）
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述（商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。）
        data.put("body", "语音交友-会员充值");
        //商户订单号
        //自定义生成商户订单号，不可重复(通过时间+随机数+用户ID生成)
        String out_trade_no = GetOrderNoUtil.getOrderIdByTimeAndUserId(user_id);
        data.put("out_trade_no", out_trade_no);
        //total_fee总金额
        data.put("total_fee", String.valueOf(total_fee));
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", NOTIFY_URL);
        //交易类型
        data.put("trade_type", TRADE_TYPE_APP);
        // //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据（可加可不加）
        // data.put("attach", attach);

        //MD5加密sign
        String sign1 = md5Util.getSign(data);
        data.put("sign", sign1);

        //调用支付接口
        try {
            /** wxPay.unifiedOrder 这个方法中调用微信统一下单接口 */
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //使用官方API请求预付订单
            Map<String, String> response = wxpay.unifiedOrder(data);
            System.out.println(response);
            String returnCode = response.get("return_code");    //获取返回码
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if (returnCode.equals("SUCCESS")) {//主要返回以下5个参数
                String resultCode = response.get("result_code");
                returnMap.put("appid", response.get("appid"));
                returnMap.put("mch_id", response.get("mch_id"));
                returnMap.put("nonce_str", response.get("nonce_str"));
                returnMap.put("sign", response.get("sign"));
                if ("SUCCESS".equals(resultCode)) {//resultCode 为SUCCESS，才会返回prepay_id和trade_type
                    //获取预支付交易回话标志
                    returnMap.put("trade_type", response.get("trade_type"));
                    returnMap.put("prepay_id", response.get("prepay_id"));

                    //保存预订单基本信息到数据库
                    RechargeOrder rechargeOrder = new RechargeOrder();
                    rechargeOrder.setOrder_number(out_trade_no);
                    rechargeOrder.setDate(new Date());
                    rechargeOrder.setMoney(total_fee);
                    rechargeOrder.setPayment(1);
                    rechargeOrder.setId(user_id);
                    rechargeOrder.setState((byte) -1);
                    rechargeOrderDao.createRechargeOrder(rechargeOrder);
                    return returnMap;
                } else {
                    //此时返回没有预付订单的数据
                    return returnMap;
                }
            } else {
                return returnMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //系统等其他错误的时候
        }
        // returnMap.put("msg","订单异常，请稍后重试");
        return returnMap;
    }

    /**
     *该链接是通过【统一下单API】中提交的参数notify_url设置
     * @param notifyData 异步通知后的XML数据
     * 1.对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”
     * 2.对收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过
     *   对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String payBack(String notifyData){
        System.out.println("获取微信回调信息开始======");
        WXpayConfig config = null;
        try {
            config = new WXpayConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            // 调用官方SDK转换成map类型数据
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            notifyMap = WXPayUtil.xmlToMap(notifyData);
            System.out.println("notifyData======"+notifyData);
            //验证签名是否有效，有效则进一步处理
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                System.out.println("商户订单号======"+out_trade_no);
                String total_fee = notifyMap.get("total_fee");//订单金额
                String transaction_id = notifyMap.get("transaction_id");//微信生成订单号
                System.out.println("total_fee===="+total_fee);
                if (return_code.equals("SUCCESS")) {
                    //判断充值金额是否正确
                    RechargeOrder rechargeOrder = rechargeOrderDao.selectBytransactionId(out_trade_no);
                    System.out.println(rechargeOrder.toString());
                    // String money = String.valueOf(rechargeOrder.getMoney());
                    String money = String.valueOf(rechargeOrder.getMoney());
                    if (out_trade_no != null && money.equals(total_fee) && notifyMap.get("appid").equals(WXpayConfig.APP_ID)) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        System.err.println("支付成功");
                        rechargeOrder.setState((byte) 0);
                        /******充值成功，账户余额需要加上******/
                        /*****************************************************************/
                        int coin_amount = coinRechargeService.coinRechargeByPrice(rechargeOrder.getMoney());
                        //更新用户账户余额
                        giftOrderService.addCoins(rechargeOrder.getId(), coin_amount);
                        UserCoins userCoins = userCoinsService.getUserCoins(rechargeOrder.getId());
                        rechargeOrder.setAccount_balance(userCoins.getUser_coins());
                        User user = userService.findOneById(rechargeOrder.getId());
                        rechargeOrder.setUsername(user.getUsername());
                        rechargeOrder.setBank_type(notifyMap.get("bank_type"));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        // SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date time_end = sdf.parse(notifyMap.get("time_end"));
                        rechargeOrder.setDate_end(time_end);
                        // rechargeOrder.setDate_end(sdf.parse(notifyMap.get("time_end")));
                        rechargeOrder.setTrade_type(notifyMap.get("trade_type"));
                        rechargeOrder.setTransaction_id(transaction_id);
                        rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
                        logger.info("微信手机支付回调成功订单号:{}", out_trade_no+"总金额为"+total_fee);
                        System.out.println("微信手机支付回调成功订单号:{"+out_trade_no+"},"+"总金额为"+total_fee);
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        return xmlBack;
                    } else {
                        rechargeOrder.setState((byte) -1);
                        rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
                        System.out.println("支付失败");
                        logger.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                        return xmlBack;
                    }
                }
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                logger.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                // return xmlBack;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    //微信查询订单,根据String transaction_id查询
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, String> orderInquiry(String transaction_id) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        logger.info("微信支付订单查询开始，查询微信订单号为" + transaction_id);
        WXpayConfig config = null;
        try {
            config = new WXpayConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        //随机字符串（官方获取随机数）
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        //商户订单号
        data.put("out_trade_no", transaction_id);
        //MD5加密sign
        WxMD5Util md5Util = new WxMD5Util();
        String sign1 = md5Util.getSign(data);
        data.put("sign", sign1);

        Map<String, String> returnMap = new HashMap();
        try {
            //查询订单
            Map<String, String> response = wxpay.orderQuery(data);
            System.out.println(response);
            String returnCode = response.get("return_code");    //获取返回码
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if (returnCode.equals("SUCCESS")) {//主要返回以下5个参数
                String resultCode = response.get("result_code");//业务结果
                returnMap.put("appid", response.get("appid"));
                returnMap.put("mch_id", response.get("mch_id"));
                returnMap.put("nonce_str", response.get("nonce_str"));
                returnMap.put("sign", response.get("sign"));

                if ("SUCCESS".equals(resultCode)) {//resultCode 为SUCCESS
                    RechargeOrder rechargeOrder = rechargeOrderDao.selectBytransactionId(response.get("out_trade_no"));
                    String money = String.valueOf(rechargeOrder.getMoney());
                    if (response.get("out_trade_no") != null && money.equals(response.get("total_fee")) && response.get("appid").equals(WXpayConfig.APP_ID)) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        System.err.println("支付成功");
                        RechargeOrder rechargeOrder1 = rechargeOrderDao.selectBytransactionId(response.get("transaction_id"));//获取微信发送过来的订单号
                        //判断之前是否已经添加过账户余额
                        if (rechargeOrder1 == null || rechargeOrder1.getTransaction_id().equals("")){//表示此订单未被受理，需添加余额
                            rechargeOrder.setState((byte) 0);
                            /******充值成功，账户余额需要加上******/
                            int coin_amount = coinRechargeService.coinRechargeByPrice(Integer.parseInt(response.get("total_fee")));
                            //添加用户账户余额
                            giftOrderService.addCoins(rechargeOrder.getId(), coin_amount);
                            UserCoins userCoins = userCoinsService.getUserCoins(rechargeOrder.getId());
                            rechargeOrder.setAccount_balance(userCoins.getUser_coins());
                            User user = userService.findOneById(rechargeOrder.getId());
                            rechargeOrder.setUsername(user.getUsername());
                            rechargeOrder.setBank_type(response.get("bank_type"));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            rechargeOrder.setDate_end(sdf.parse(response.get("time_end")));
                            rechargeOrder.setTrade_type(response.get("trade_type"));
                            rechargeOrder.setTransaction_id(transaction_id);
                            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
                            logger.info("微信手机支付回调成功订单号:{}", response.get("out_trade_no") + "总金额为" + response.get("total_fee"));
                            //获取支付状态并更新保存
                            this.paymentState(response.get("transaction_id"), response.get("trade_state"));
                            return returnMap;
                        }else {
                            return returnMap;
                        }
                    } else {
                        //此时返回没有预付订单的数据
                        logger.error(returnMap.toString());
                        return returnMap;
                    }
                }else {
                    logger.error(returnMap.toString());
                    return returnMap;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error("erroroMsg:--- 微信支付订单查询失败" + e.getMessage());
        }
        return returnMap;
    }



    /**
     * 判断支付状态,并更改数据库状态
     * @param transaction_id
     * @param trade_state
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String paymentState(String transaction_id,String trade_state) throws Exception{
        //根据订单号查询订单
        RechargeOrder rechargeOrder = rechargeOrderDao.selectBytransactionId(transaction_id);
        if ("SUCCESS".equals(trade_state )){
            rechargeOrder.setState((byte) 0);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "支付成功";
        }else if ("REFUND".equals(trade_state )){
            rechargeOrder.setState((byte) 1);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "转入退款";
        }
        else if ("NOTPAY".equals(trade_state )){
            rechargeOrder.setState((byte) 2);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "未支付";
        }
        else if ("CLOSED".equals(trade_state )){
            rechargeOrder.setState((byte) 3);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "已关闭";
        }
        else if ("REVOKED".equals(trade_state )){
            rechargeOrder.setState((byte) 4);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "已撤销（刷卡支付）";
        }
        else if ("USERPAYING".equals(trade_state )){
            rechargeOrder.setState((byte) 5);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "用户支付中";
        } else if("PAYERROR".equals(trade_state )){
            rechargeOrder.setState((byte) -1);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "支付失败";
        }else {
            rechargeOrder.setState((byte) -1);
            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
            return "支付失败";
        }
    }
}
