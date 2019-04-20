package com.recharge_cash.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {
    //    1、商户appid
    @Value("")
    // public static String APPID="2019032863711346";
    public static String APPID="2016092600598524";
    //    2、app私钥pkcs8格式
    @Value("")
    public static String RSA_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCuEWOsrDHRKFSNpm2i9Gpy0zG7wKgiYRA5Zx17J26bHQABrH6q9CT22ed9lYzODV44bcGn4wbFRtkqMb0Vm+lAPyqsyDpLuLIYizeaHJUFhYUFQuCwzalIUCc8DKIqodfjDSSGLW/VLE8rWj5swiO3PTWx7LRIanPxgLnobZINWRoS/EXPpubhx9vV7QHIxglW9G6cBof0/qqbBwjNZSRLDiIeOI2aPsFPdFUZNxI1hmcvHpqImdpO6q2JLRIOP1oaGHPQ3Px/PF1Z3MVZYMiKjzF9S/kE6JmWX0gjDTGPJoa3Oak0S93ortvzMF9durKoJVVjzHZIIcM/pJ28ITfrAgMBAAECggEAAfXMK4uc3AAaQ6YWIfmioTxjmPuoaQqyxhK1MEfXZQPElkA9BOvO5sC5bJ6cgU2BPB1wStWxo9MSavk4ad/hlomfj2aJ0YR47PHUZHXh77f7MX35TcRjHZ9ZccGqqdQ8vB/Pw3sLgVkIYzKW9V6STGPyENjbht08BQIwFb/2HxRXBBo2qu7JsXyaly3N2vLdMPO53hii+Puqxxcw64GGIjH5FmU/3faGT/l3o3jZ95GV2a0ToLsCFe5IsCjF6K+jMds/osKXUyQJ7GjUT0e3zMbA/ciHCw+g+f0XLzrtc8kmd9FaGRNyqJ/7K+qjnWSk+j2ucwXa5ll+IDZ09hgbsQKBgQDkCA9s3nAE0L2cXoq6s2jJH6LCvgss+tKbVvQ6f4IGU1/vx02DEqZUQa/LJ0pmmZdrycKOWff3TWB8j6JLj4Nj5zYSt2h84oflgo4AxrmZth1GG1WCAmvQLxDpLJv8lfRYal2em02TCMaKjVSssXdSCGMqIGpXGdVSZatCJXof1QKBgQDDau6pXlMcP2tZNghsSSWzcfiVumzDLJheyUmcHHnKHn6TVs0eOnOPkajsIlF2CxdsCtkqxljCEsF6XD5v9jgv9Pw5jTxGH1qaX7f4kUGEr3u2eFSHzYcuIef68u88W4B9DcoIXi5BIvseydOJCSNV6RG2d09quv9EjXUigimYvwKBgHOOLhrygfu+uEeUlWLVZgV91aIwYkNx++YiY3N9iCuidpxw+DOy8L0D6IzfpVFnBhfmT4SJzSHygTG+K/V0jZXD5mAHt5iJx2okBcgY7JcbdaxAT82pYm9GWqB/LbELYJ6pkDW+58naCZrO0hkNebUFcRM5CAqJmdO+l9HWYhPdAoGAWhX9l86H+cmyrxmdoG3IF4U+16dtZDw2sduRN4pZCrgsGbg2Lu07QMAhY1oeu5A5Z/FIU9PELxN+Ze4ISozzMgh6Df3x/1GM7wWZRdmQnp5XJbMrJL+2xamTy7gaWXTFMuEcdecU46pAodzgy5eO1pfvNnKiBc3Wx79+dD4U9vkCgYA3zpopUnOCBGmZA9spOPFhYTvwxjfkpjO5iYqltQh7N5L/EE3OZdb272gCy44k223ly0Mwwi8k5/VTmXUFGN5WQWlYdaf+O6GbcN9c1kjY1nO+d9/Wz3o7rqU/nAWrawGPOVcdtNRyy5j+MiemUhTsZt1GRmjpBufPGGND7uXFUA==";
    //    3、支付宝公钥
    @Value("")
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz8I0M9IF0/v66wV8fKnjSei1srjXJZNDjEAguDdJ69/ZFwySsb3L4cDEaLLj5t8cOtRsA/dRsZKxM8Z8LXsmzXtVffdkOTxhrzPiIuKOm22Aiw87BKHTyCTmwNmp2lHVYFgesAQDFpMLlEInaE2cOwk7GJentQOsq1QamtO5mk03kHJbH+tw+bmXJGZvcO7d0CPUhL/J8UDFOLA+zkIqRMK3rkiWEaat8n4YLuwGHPnyAjGNt8/bY1Q0MbUYpFBMsjk/Q8KKE/YlVPcL/+3hJ7UsHVvmgdlokLLTcFzYt7G88UbnCv6Lq+5aHYCJmOPU3x9W6GMNxpMwy3rv/pCMKQIDAQAB";
    // 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("")
    public static String notify_url = "http://129.204.52.213:8081/kol_recharge_cash/Pay/aliPayNotify_url";
    //5.页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    @Value("")
    public static String return_url = "http://129.204.52.213:8081/kol_recharge_cash/Pay/aliPayNotify_url";
    // 6.请求支付宝的网关地址
    @Value("")
    public static String URL = "https://openapi.alipay.com/gateway.do";
    //    7、编码
    @Value("")
    public static String CHARSET = "utf-8";
    //    8、返回格式
    @Value("")
    public static String FORMAT = "json";
    //    9、加密类型
    @Value("")
    public static String SIGNTYPE = "RSA2";
    @Value("")
    // public static String SELLERID = "2088431557976319";
    public static String SELLERID = "2088102177309659";
}
