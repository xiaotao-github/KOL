package com.kol_user.controller;


import com.auth0.jwt.interfaces.DecodedJWT;


import com.kol_user.comment.JsonUtils;
import com.kol_user.comment.TokenUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    JedisPool jedisPool;

    /**
     * 对用户的具体拦截功能
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        httpServletResponse.setCharacterEncoding("utf-8");
        //则进行判断用户之前是否登录过，登录过则放行，没有登录则拦截
        Jedis jedis = jedisPool.getResource();
        Map map = new HashMap();
        PrintWriter out = null;
        try {
            String token = httpServletRequest.getHeader("token");
            if (token!=null && token.length()>0){
                DecodedJWT jwt = TokenUtil.parseJWT(token);
                if (jwt != null) {
                    String user_id = jwt.getClaim("user_id").asString();
                    //判断token是否存在
                    if (!TextUtils.isEmpty(token) && jedis.hexists("userToken",user_id) && jedis.hmget("userToken",user_id).get(0).equals(token)){
                        //正确，放行
                        return true;
                    }else {
                        map.put("status", 405);
                        map.put("data", null);
                        map.put("msg", "token不存在，请重新登录");
                        out = httpServletResponse.getWriter();
                        out.append(JsonUtils.objectToJson(map));
                    }
                }else {
                    map.put("status", 405);
                    map.put("data", null);
                    map.put("msg", "登录信息失效，token校验错误，请重新登录");
                    out = httpServletResponse.getWriter();
                    out.append(JsonUtils.objectToJson(map));
                }
            }else {
                map.put("status", 405);
                map.put("data", null);
                map.put("msg", "登录信息失效，请重新登录");
                out = httpServletResponse.getWriter();
                out.append(JsonUtils.objectToJson(map));
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("status",400);
            map.put("data",null);
            map.put("msg","系统错误，请稍后重试");
            out = httpServletResponse.getWriter();
            out.append(JsonUtils.objectToJson(map));
        }finally {
            jedis.close();
        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
