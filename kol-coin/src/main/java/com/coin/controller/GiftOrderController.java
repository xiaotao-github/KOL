package com.coin.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.coin.comment.JsonUtils;
import com.coin.comment.TokenUtil;
import com.coin.dto.GiftOrder;
import com.coin.dto.Response;
import com.coin.service.GiftOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/giftOrder")
public class GiftOrderController {
    @Autowired
    GiftOrderService giftOrderService;

////    用户赠送礼物
////    接受者为一个数组，因为可能送给很多人
//    @RequestMapping("/giveGift")
//    public String giveGift(@RequestParam String user_id,@RequestParam String[] receiver_id,@RequestParam String gift_id,@RequestParam int num){
//        String res= giftOrderService.giveGift(user_id,receiver_id,gift_id,num);
//        return res;
//    }


    //排行榜列表
    //type:榜单类型 1--魅力榜 2--贡献榜
    //time:时间范围 1--总榜  2--周榜
    @RequestMapping("/getRankingList")
    @ResponseBody
    public Response getRankingList(HttpServletRequest request, int type, int time, @RequestParam(defaultValue = "1") int pageNo) {
        Response response;
//        int pageSize = 10;
        // String token = request.getHeader("token");
        // DecodedJWT jwt = TokenUtil.parseJWT(token);
        // if (jwt != null) {
        try {
            if (type == 1 && time == 1) {
                //魅力榜总榜
//                    PageHelper.startPage(pageNo, pageSize);
//                    PageInfo<RankingList> pageInfo=new PageInfo<>(giftOrderService.totalCharm());
                return new Response("魅力榜总榜查询成功", JsonUtils.objectToJson(giftOrderService.totalCharm()), 200);
            } else if (type == 1 && time == 2) {
                //魅力榜周榜
                    /*PageHelper.startPage(pageNo, pageSize);
                    PageInfo<RankingList> pageInfo=new PageInfo<>(giftOrderService.weekCharm());
                    if (pageInfo.getList()!=null && pageInfo.getList().size()>0){
                        if (pageNo<=pageInfo.getLastPage()){
                            response = new Response("魅力榜周榜查询成功",JsonUtils.objectToJson(giftOrderService.weekCharm()),200);
                        }else {
                            response = new Response("魅力榜周榜查询结束","[]",200);
                        }
                    }else {
                        response = new Response("系统错误，请稍后重试", null, 400);
                    }*/
                return new Response("魅力榜周榜查询成功", JsonUtils.objectToJson(giftOrderService.weekCharm()), 200);
            } else if (type == 2 && time == 1) {
                //贡献榜总榜
               /* PageHelper.startPage(pageNo, pageSize);
                PageInfo<RankingList> pageInfo = new PageInfo<>(giftOrderService.totalContribution());
                if (pageNo <= pageInfo.getLastPage()) {
                    response = new Response("贡献榜总榜查询成功", JsonUtils.objectToJson(giftOrderService.totalContribution()), 200);
                } else {
                    response = new Response("贡献榜总榜查询结束", "[]", 200);
                }*/
                return new Response("贡献榜总榜查询成功", JsonUtils.objectToJson(giftOrderService.totalContribution()), 200);
            } else if (type == 2 && time == 2) {
                //贡献榜周榜
                /*PageHelper.startPage(pageNo, pageSize);
                PageInfo<RankingList> pageInfo = new PageInfo<>(giftOrderService.weekContribution());
                if (pageInfo.getList() != null && pageInfo.getList().size() > 0) {
                    if (pageNo <= pageInfo.getLastPage()) {
                        response = new Response("贡献榜周榜查询成功", JsonUtils.objectToJson(pageInfo.getList()), 200);
                    } else {
                        response = new Response("贡献榜周榜查询结束", "[]", 200);
                    }
                } else {
                    response = new Response("系统错误，请稍后重试", null, 400);
                }*/
                return new Response("贡献榜周榜查询成功", JsonUtils.objectToJson(giftOrderService.weekContribution()), 200);
            } else {
                response = new Response("参数错误，请稍后重试", null, 400);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试", null, 400);
            return response;
        }
        // }else {
        //     response = new Response("登录信息失效，请重新登录", null, 405);
        //     return response;
        // }
    }


    //    查询用户收到过的礼物
    @RequestMapping("/findAcceptGiftByUser")
    @ResponseBody
    public Response findAcceptGiftByUser(HttpServletRequest request, @RequestParam(defaultValue = "1") int pageNo) {
        Response response;
        int pageSize = 10;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        try {
            if (jwt != null) {
                String user_id = jwt.getClaim("user_id").asString();
                PageHelper.startPage(pageNo, pageSize);
                List list = giftOrderService.findAcceptGiftByUser(user_id);
                PageInfo<GiftOrder> pageInfo = new PageInfo<>(list);
                if (pageNo <= pageInfo.getLastPage()) {
                    response = new Response("查询用户收到的礼物", JsonUtils.objectToJson(pageInfo.getList()), 200);
                } else {
                    response = new Response("查询用户收到的礼物列表结束", "[]", 200);
                }
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统异常，请稍后重试", null, 400);
        }
        return response;
    }

    //查询用户送出的礼物
    @RequestMapping("/findGiveGiftByUser")
    @ResponseBody
    public Response findGiveGiftByUser(HttpServletRequest request, @RequestParam(defaultValue = "1") int pageNo) {
        Response response;
        int pageSize = 10;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        try {
            if (jwt != null) {
                String user_id = jwt.getClaim("user_id").asString();
                PageHelper.startPage(pageNo, pageSize);
                List list = giftOrderService.findGiveGiftByUser(user_id);
                PageInfo<GiftOrder> pageInfo = new PageInfo<>(list);
                if (pageNo <= pageInfo.getLastPage()) {
                    response = new Response("查询用户送出的礼物", JsonUtils.objectToJson(pageInfo.getList()), 200);
                } else {
                    response = new Response("查询用户送出的礼物列表结束", "[]", 200);
                }
                return response;
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统异常，请稍后重试", null, 400);
            return response;
        }
    }


    //查询用户收到的礼物(用于用户主页展示)
    @RequestMapping("/findGiveGiftByUserToUser")
    @ResponseBody
    public Response findGiveGiftByUserToUser(HttpServletRequest request) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        try {
            if (jwt != null) {
                String user_id = jwt.getClaim("user_id").asString();
                List list = giftOrderService.findGiveGiftByUserToUser(user_id);
                response = new Response("查询用户收到的礼物", JsonUtils.objectToJson(list), 200);
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统异常，请稍后重试", null, 400);
        }
        return response;
    }

    //    用户的礼物界面，可以看出用户送出了什么礼物和收到了什么礼物
    // @RequestMapping("/findGiftListByUser")
    // @ResponseBody
    // public Response findGiftListByUser(HttpServletRequest request,@RequestParam(defaultValue="1")int pageNo){
    //     Response response;
    //     int pageSize = 10;
    //     String token = request.getHeader("token");
    //     DecodedJWT jwt = TokenUtil.parseJWT(token);
    //     if (jwt != null) {
    //         String user_id = jwt.getClaim("user_id").asString();
    //         try {
    //             response = new Response("查看用户送出礼物列表与收到礼物列表", giftOrderService.findGiftListByUser(user_id), 200);
    //             return response;
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //             response = new Response("系统异常，请稍后重试", null, 400);
    //             return response;
    //         }
    //     }else {
    //         response = new Response("登录信息失效，请重新登录", null, 405);
    //         return response;
    //     }
    // }

    //金币充值界面列表（X币兑换比例）
    //注意请求头,如何是text/json会出现406错误
    @RequestMapping(value = "/coinRecharge", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Response coinRecharge(HttpServletRequest request) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            try {
                response = new Response("金币充值界面列表", giftOrderService.coinRecharge(), 200);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                response = new Response("系统异常，请稍后重试", null, 400);
                return response;
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
            return response;
        }
    }

    //提供礼物值接口
    @RequestMapping("/getTotalCharmValue")
    @ResponseBody
    public Response getTotalCharmValue(String user_id) {
        Response response;
        try {
            response = new Response("礼物值接口", giftOrderService.getTotalCharmValue(user_id), 200);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统异常，请稍后重试", null, 400);
            return response;
        }
    }

    //充值订单生成
}
