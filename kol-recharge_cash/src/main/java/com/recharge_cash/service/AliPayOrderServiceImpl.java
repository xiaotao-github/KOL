package com.recharge_cash.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;

import com.coin.dto.UserCoins;
import com.coin.service.GiftOrderService;
import com.coin.service.UserCoinsService;
import com.kol_user.dto.User;
import com.kol_user.service.UserService;
import com.recharge_cash.comment.AlipayConfig;
import com.recharge_cash.comment.GetOrderNoUtil;
import com.recharge_cash.dao.RechargeOrderDao;
import com.recharge_cash.dto.RechargeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class AliPayOrderServiceImpl implements AliPayOrderService {
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

    private final static Logger logger= LoggerFactory.getLogger(AliPayOrderServiceImpl.class);

    /**
     * 获取支付宝加签后台的订单信息字符串
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String getAliPayOrderStr(String total_fee,String user_id){
        String orderString = "";
//        最终返回加签之后的，app需要传给支付宝app的订单信息字符串
        String out_trade_no = GetOrderNoUtil.getOrderIdByTimeAndUserId(user_id);
        logger.info("支付宝下单，商户订单号为："+ out_trade_no);
        try{
    //         实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型），为了取得预付订单信息
             AlipayClient alipayClient=new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY,
                      AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
    //         实例化具体API对应的request类，类名称和接口名称对应，当前调用接口名称：alipay.trade.app.pay
             AlipayTradeAppPayRequest ali_request=new AlipayTradeAppPayRequest();
    //         SDK已经封装了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式
             AlipayTradeAppPayModel model=new AlipayTradeAppPayModel();
    //         业务参数传入，可以传很多，参考API
    //         model.setPassbackParams(URLEncoder.encode(request.getBody().toString()));//公用参数（附加数据）
             //model.setBody(moneyOrder.getBody());//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
             model.setSubject("语音交友-会员充值");//商品名称
             model.setOutTradeNo(out_trade_no);//商户订单号（自动生成）
    //         model.setTimeoutExpress("30m");//交易超时时间
             model.setTotalAmount(total_fee);//支付金额
             model.setProductCode("QUICK_MSECURITY_PAY");//销售产品码（固定值）
             ali_request.setBizModel(model);//
             System.out.println("异步通知的地址为："+AlipayConfig.notify_url);
             ali_request.setNotifyUrl(AlipayConfig.notify_url);//异步回调地址（后台）
             // ali_request.setReturnUrl(AlipayConfig.return_url);//同步回调地址（APP）

             //这里和普通的接口调用不同，使用的是sdkExecute
             AlipayTradeAppPayResponse alipayTradeAppPayResponse=alipayClient.sdkExecute(ali_request);//返回支付宝订单信息（预处理）
             orderString=alipayTradeAppPayResponse.getBody();//就是orderString可以直接给APP请求，无需再做处理。
             //创建商户支付宝订单(因为需要记录每次支付宝支付的记录信息，单独存一个表跟商户订单表关联，以便以后查证)
             RechargeOrder rechargeOrder = new RechargeOrder();
             rechargeOrder.setOrder_number(out_trade_no);//商户订单号
             rechargeOrder.setState((byte) -1);//交易状态
             rechargeOrder.setMoney(Integer.parseInt(total_fee));//订单金额
             rechargeOrder.setDate(new Date());
             rechargeOrder.setPayment(2);
             rechargeOrder.setId(user_id);
             rechargeOrderDao.createRechargeOrder(rechargeOrder);
        }catch (AlipayApiException e){
             e.printStackTrace();
             logger.info("与支付宝交互出错，未能生成订单，请检查代码！");
        }
        System.out.println("aliPay===**===="+orderString);
        return orderString;
    }
    /*
    * 支付宝异步请求逻辑处理
    * @param request
    * @return
    * @throws IOException
    * */
    @Transactional(propagation = Propagation.REQUIRED)
    public String notify(Map<String,String> conversionParams){
        System.out.println("支付宝异步请求逻辑处理开始");
        logger.info("支付宝异步请求逻辑处理");
        //签名验证（对支付宝返回的数据验证，确定是支付宝返回的）
        boolean signVerified=false;
        try{
            //调用SDK验证签名
            signVerified= AlipaySignature.rsaCheckV1(conversionParams, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            //对验签进行处理
            if (signVerified){
                //验签通过
                //获取需要保存的数据
                String appId=conversionParams.get("app_id");//支付宝分配给开发者的应用Id
                String notifyTime=conversionParams.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
                String gmtCreate=conversionParams.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
                String gmtPayment=conversionParams.get("gmt_payment");//交易付款时间
                String gmtRefund=conversionParams.get("gmt_refund");//交易退款时间
                String gmtClose=conversionParams.get("gmt_close");//交易结束时间
                String tradeNo=conversionParams.get("trade_no");//支付宝的交易号
                String outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
                String outBizNo=conversionParams.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
                String buyerLogonId=conversionParams.get("buyer_logon_id");//买家支付宝账号
                String sellerId=conversionParams.get("seller_id");//卖家支付宝用户号
                String sellerEmail=conversionParams.get("seller_email");//卖家支付宝账号
                String totalAmount=conversionParams.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
                String receiptAmount=conversionParams.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
                String invoiceAmount=conversionParams.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
                String buyerPayAmount=conversionParams.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
                String tradeStatus = conversionParams.get("trade_status");// 获取交易状态
                //支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）
                // AlipaymentOrder alipaymentOrder=this.selectByOutTradeNo(outTradeNo);
                RechargeOrder rechargeOrder = rechargeOrderDao.selectBytransactionId(outTradeNo);
                //处理数据库与支付宝传递的金额小数点问题
                DecimalFormat sdf2 = new DecimalFormat("######0.00");
                Double s = Double.valueOf(rechargeOrder.getMoney());
                String money = sdf2.format(s);
                if (rechargeOrder.getOrder_number()!=null && totalAmount.equals(money) && AlipayConfig.APPID.equals(appId) && sellerId.equals(AlipayConfig.SELLERID)){
                    System.out.println(tradeStatus);
                    try {
                        if (tradeStatus.equals("TRADE_SUCCESS")){//只处理支付成功的订单：修改交易表状态，支付成功
                            rechargeOrder.setState((byte) 0);
                            /******充值成功，账户余额需要加上******/
                            //去掉后面两位小数点进行充值档位的查找
                            double i = Double.valueOf(totalAmount);
                            int coin_price = (int) i;
                            int coin_amount = coinRechargeService.coinRechargeByPrice(coin_price);
                            //添加用户账户余额
                            giftOrderService.addCoins(rechargeOrder.getId(), coin_amount);
                            UserCoins userCoins = userCoinsService.getUserCoins(rechargeOrder.getId());
                            rechargeOrder.setAccount_balance(userCoins.getUser_coins());
                            User user = userService.findOneById(rechargeOrder.getId());
                            rechargeOrder.setUsername(user.getUsername());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                rechargeOrder.setDate_end(sdf.parse(gmtPayment));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            rechargeOrder.setTransaction_id(tradeNo);
                            rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
                            System.err.println("支付成功");
                            return "success";
                        } else {
                            return "fail";
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("系统异常");
                        return "fail";
                    }
                }else{
                    System.out.println("支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）,不一致！返回fail");
                    return "fail";
                }
            }else {
                System.out.println("验签不通过");
                return "fail";
            }
        }catch (AlipayApiException e){
            System.out.println("验签失败");
            e.printStackTrace();
            return "fail";
        }
    }
    /*
    * 用支付宝发起了订单查询请求
    * @param request
    * @return
    * @throws IOException
    * */
    public Byte checkAlipay(String outTradeNo){
        System.out.println("向支付宝发起查询，查询商户订单号为："+outTradeNo);
        logger.info("向支付宝发起查询，查询商户订单号为："+outTradeNo);
        try{
            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                    AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                    AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeQueryRequest alipayTradeQueryRequest=new AlipayTradeQueryRequest();
            alipayTradeQueryRequest.setBizContent("{" +
                    "\"out_trade_no\":\""+outTradeNo+"\"" +
                    "}");
            //查询订单开始
            AlipayTradeQueryResponse alipayTradeQueryResponse=alipayClient.execute(alipayTradeQueryRequest);
            if (alipayTradeQueryResponse.isSuccess()){
                RechargeOrder rechargeOrder = rechargeOrderDao.selectBytransactionId(alipayTradeQueryResponse.getOutTradeNo());
                RechargeOrder rechargeOrder1 = rechargeOrderDao.selectBytransactionId(alipayTradeQueryResponse.getTradeNo());//获取支付宝发送过来的订单号
                /******充值成功，账户余额需要加上******/
                //判断之前是否已经添加过账户余额
                if (rechargeOrder1 == null || rechargeOrder1.getTransaction_id().equals("")) {//表示此订单未被受理，需添加余额
                    int coin_amount = coinRechargeService.coinRechargeByPrice(Integer.valueOf(alipayTradeQueryResponse.getTotalAmount()));
                    //添加用户账户余额
                    giftOrderService.addCoins(rechargeOrder.getId(), coin_amount);
                    UserCoins userCoins = userCoinsService.getUserCoins(rechargeOrder.getId());
                    rechargeOrder.setAccount_balance(userCoins.getUser_coins());
                    User user = userService.findOneById(rechargeOrder.getId());
                    rechargeOrder.setUsername(user.getUsername());
                    rechargeOrder.setDate_end(alipayTradeQueryResponse.getSendPayDate());
                    rechargeOrder.setTransaction_id(alipayTradeQueryResponse.getTradeNo());
                    switch (alipayTradeQueryResponse.getTradeStatus()) // 判断交易结果
                    {
                        case "TRADE_FINISHED": // 交易结束并不可退款
                            rechargeOrder.setState((byte) 3);
                            break;
                        case "TRADE_SUCCESS": // 交易支付成功
                            rechargeOrder.setState((byte) 0);
                            break;
                        case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
                            rechargeOrder.setState((byte) -1);
                            break;
                        case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
                            // alipaymentOrder.setTradeStatus((byte) 0);
                            rechargeOrder.setState((byte) 1);
                            break;
                        default:
                            break;
                    }
                    rechargeOrderDao.updateByPrimaryKey(rechargeOrder);
                    return rechargeOrder.getState();
                }else {
                    return rechargeOrder.getState();
                }
            } else {
                logger.info("==================调用支付宝查询接口失败！");
            }
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
}
