package com.kol_friends.comment;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {
    public static String SECRET="xigua";

    //设置过期时长
    private static final long EXPIRE_TIME = 1000*60*60*24*7;

    //生成token
    public static String getJwtToken(String user_id){
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            //私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>(2);
            header.put("typ","JWT");
            header.put("alg","HS256");
            //附带信息,生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("user_id",user_id)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    //解析token返回用户对象信息
    public static DecodedJWT parseJWT(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT verify = verifier.verify(token);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //判断token是否正确
    public static Boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}