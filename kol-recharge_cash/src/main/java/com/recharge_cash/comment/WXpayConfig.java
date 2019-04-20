package com.recharge_cash.comment;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class WXpayConfig implements WXPayConfig {
    /** 加载证书  这里证书需要到微信商户平台进行下载*/
    private byte[] certData;
    public static final String APP_ID = "wx9c7f7c30b36838e9";
    public static final String KEY = "xiguagame2019hainanhuixing201903";
    public static final String MCH_ID = "1528340101";

     /*public WXpayConfig() throws Exception {
         String certPath = "D:/WXPEM";//从微信商户平台下载的安全证书存放的路径
         File file = new File(certPath);
         InputStream certStream = new FileInputStream(file);
         this.certData = new byte[(int) file.length()];
         certStream.read(this.certData);
         certStream.close();
     }*/

    @Override
    public String getAppID() {
        return APP_ID;
    }

    //parnerid，商户号
    @Override
    public String getMchID() {
        return MCH_ID;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 0;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 0;
    }
}
