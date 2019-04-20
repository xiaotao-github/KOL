package com.kol_room.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.coin.dto.GiftOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kol_room.comment.JsonUtils;
import com.kol_room.comment.SLEmojiFilter;
import com.kol_room.comment.TokenUtil;
import com.kol_room.dto.Response;
import com.kol_room.dto.RoomInfos;
import com.kol_room.service.UserRoomService;
import com.kol_user.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

//App中用户房间中的操作
@Controller
@RequestMapping("/room")
public class UserRoomController {
    @Autowired
    private UserRoomService userRoomService;

    /***
     * 获取token信息
     * @param request
     * @return
     */
    public Map<String, Object> getTokenMsg(HttpServletRequest request) {
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        Map<String, Object> roomPlayload = new HashMap<>();
        if (jwt != null) {
            User user = userRoomService.getOneUser(jwt.getClaim("user_id").asString());
            roomPlayload.put("uid", user.getId());
            roomPlayload.put("name", user.getUsername());
            roomPlayload.put("avatar", user.getUser_image());
        }
        return roomPlayload;
    }

    //预读房间
    @RequestMapping("/pre_info")
    @ResponseBody
    public Response preInfo(HttpServletRequest request, String roomId) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        String user_id;
        if (jwt != null) {
            user_id = jwt.getClaim("user_id").asString();
//      根据房间id来查找
            Map<String, Object> room = userRoomService.preInfo(roomId, user_id);
            //判断map是否为空
            if (room == null || room.size() < 1) {
                response = new Response("房间不存在", 400);
            } else {
                response = new Response("获取成功", JsonUtils.objectToJson(room), 200);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", 405);
        }
        return response;
    }

    //创建房间
    @RequestMapping("/createRoom")
    @ResponseBody
    public Response createRoom(HttpServletRequest request, String room_label, String limit, String room_name, @RequestParam(required = false, value = " ") String room_topic) {
        Response response = new Response();
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        String user_id;
        if (jwt != null) {
            user_id = jwt.getClaim("user_id").asString();
            try {
                if (room_label != null && limit != null && room_name != null) {
                    response = userRoomService.createRoom(user_id, room_label, limit, room_name, room_topic);
                } else {
                    response = new Response("创建房间失败,缺少参数", 400);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(400);
                response.setMsg("系统出错，请稍后重试");
                response.setData(null);
            }
        } else {
            response.setStatus(405);
            response.setMsg("登录信息失效，请重新登录");
            response.setData(null);
        }
        return response;
    }

    //    房间列表
    @RequestMapping("/showRoomList")
    @ResponseBody
    public Response showRoomList(HttpServletRequest request,int pageNo, @RequestParam(defaultValue = "0", required = false) int label) {
        String phonename = request.getHeader("User-Agent");
        System.out.println("phonename===>>>"+phonename);
        Response response = new Response();
        try {
            if (label > 0) {
                response = userRoomService.selectRoomList(label, pageNo);
            } else {
                response = userRoomService.selectRoomList(0, pageNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            response.setMsg("系统出错，请稍后重试");
        }
        return response;
    }

    //进入房间
    @RequestMapping("joinRoom")
    @ResponseBody
    public Response joinRoom(String roomId, HttpServletRequest request) {
        Response response = new Response();
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            try {
                String user_id = jwt.getClaim("user_id").asString();
                response = userRoomService.joinRoom(roomId, user_id);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(400);
                response.setMsg("系统出错，请稍后重试");
                response.setData(null);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }

    //    房间在线成员列表
    @RequestMapping("/showRoomPeopleList")
    @ResponseBody
    public Response showRoomPeopleList(String roomId) {
        Response response = new Response();
        try {
            if (roomId != null) {
                response = userRoomService.showRoomPeopleList(roomId);
            } else {
                response = new Response("缺少必须参数", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            response.setMsg("系统出错，请稍后重试");
            response.setData(null);
        }
        return response;
    }

    //退出当前房间
    @RequestMapping("/quitRoom")
    @ResponseBody
    public Response quitRoom(HttpServletRequest request,String roomId) {
        System.out.println("退出房间");
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String uid = jwt.getClaim("user_id").asString();
            try {
                return userRoomService.quitRoom(uid,roomId);
            } catch (Exception e) {
                return new Response("系统出错，请稍后重试",400);
            }
        } else {
            return new Response("登录信息失效，请重新登录", null, 405);
        }
    }

    //改到这里
    //    解散房间
    @RequestMapping("dissolve")
    @ResponseBody
    public Response dissolve(HttpServletRequest request) {
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String uid = jwt.getClaim("user_id").asString();
            try {
                return userRoomService.dissolve(uid);
            } catch (Exception e) {
                return new Response("系统出错，请稍后重试",400);
            }
        } else {
            return new Response("登录信息失效，请重新登录", null, 405);
        }
    }

    //获取背景图片列表
    @RequestMapping("/getImageList")
    @ResponseBody
    public Response getImageList() {
        try {
            List<String> list = new ArrayList<>();
            list.add("http://129.204.52.213/room_image/bg_room_0.png");
            list.add("http://129.204.52.213/room_image/bg_room_1.png");
            list.add("http://129.204.52.213/room_image/bg_room_2.png");
            list.add("http://129.204.52.213/room_image/bg_room_3.png");
            return new Response("获取成功", JsonUtils.objectToJson(list), 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        }
    }

    //    显示麦位信息
    @RequestMapping("/show_microphone")
    @ResponseBody
    public Response show_microphone(String roomId) {
        System.out.println("显示麦位信息");
        Response response = new Response();
        try {
            if (roomId != null) {
                List microphones = userRoomService.show_microphone(roomId);
                if (microphones != null && microphones.size() > 0) {
                    String data = JsonUtils.objectToJson(microphones);
                    response.setStatus(200);
                    response.setMsg("获取麦位成功");
                    response.setData(data);
                } else {
                    response = new Response("没有这个房间", null, 400);
                }
            } else {
                response = new Response("缺少必须参数", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            response.setMsg("系统出错，请稍后重试");
            response.setData(null);
        }
        // Response response=new Response("房间麦位信息",data,200);
        // return response.toString();
        return response;
    }

    /***
     * 邀请用户上麦
     */
    @RequestMapping("/invite")
    @ResponseBody
    public Response invite(String user_id) {
        Response response = new Response();
        try {
            userRoomService.invite(user_id);
            response.setStatus(200);
            response.setMsg("邀请成功");
            response.setData(null);
        } catch (Exception e) {
            response.setStatus(400);
            response.setMsg("系统出错，请稍后重试");
            response.setData(null);
        }
        return response;
    }

    /***
     * 上麦
     */
    @RequestMapping("/invite_micro")
    @ResponseBody
    public Response up_phone(HttpServletRequest request, @RequestParam String roomId, @RequestParam int position) {
        //    上麦
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String uid = jwt.getClaim("user_id").asString();
            //    上麦
            try {
                return userRoomService.up_phone(uid, position, roomId, roomId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Response("系统出错，请稍后重试", null, 400);
            }
        } else {
            return new Response("登录信息失效，请重新登录", null, 405);
        }
    }

    //请人下麦
    @RequestMapping("/request_down_micro")
    @ResponseBody
    public Response request_down_micro(HttpServletRequest request, @RequestParam String roomId, @RequestParam int position) {
        Response response = new Response();
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String uid = jwt.getClaim("user_id").asString();
            try {
                userRoomService.request_down_phone(roomId, position, uid);
                response.setStatus(200);
                response.setMsg("发送下麦请求成功");
                response.setData(null);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(400);
                response.setMsg("系统出错，请稍后重试");
                response.setData(null);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }

    //下麦
    @RequestMapping("/down_micro")
    @ResponseBody
    public Response down_phone(HttpServletRequest request, @RequestParam String roomId) {
        Response response = new Response();
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String uid = jwt.getClaim("user_id").asString();
            try {
                userRoomService.down_phone(roomId, uid);
                response.setStatus(200);
                response.setMsg("下麦成功");
                response.setData(null);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(400);
                response.setMsg("系统出错，请稍后重试");
                response.setData(null);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }

    //房间信息
    @RequestMapping("/findRoomById")
    @ResponseBody
    public Response findOneById(@RequestParam String roomId, HttpServletRequest request) {
        System.out.println("房间信息");
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            try {
                //      根据房间id来查找
                String user_id = jwt.getClaim("user_id").asString();
                response = userRoomService.findOneById(roomId, user_id);
            } catch (Exception e) {
                e.printStackTrace();
                response = new Response("系统出错，请稍后重试", null, 400);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }

    //打开|关闭 计数器
    @RequestMapping("/Counter")
    @ResponseBody
    public Response Counter(@RequestParam String roomId) {
        Response response;
        try {
            response = userRoomService.Counter(roomId);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

    //用户进行排麦
    @RequestMapping("/platoon")
    @ResponseBody
    public Response platoon(HttpServletRequest request, @RequestParam String roomId) {
        Response response;
        try {
            Map<String, Object> roomPlayload = getTokenMsg(request);
            response = userRoomService.platoon(roomPlayload, roomId);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

    //显示排麦的列表
    @RequestMapping("/platoonList")
    @ResponseBody
    public Response platoonList(@RequestParam String roomId) {
        Response response;
        try {
            response = userRoomService.platoonList(roomId);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

    //    封麦
    @RequestMapping("/ban_micro")
    @ResponseBody
    public Response ban_microphones(@RequestParam String roomId, @RequestParam int position) {
        try {
            return userRoomService.ban_microphones(roomId, position);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        }
    }

    //    禁麦
    @RequestMapping("/close_micro")
    @ResponseBody
    public Response cloase_micro(@RequestParam String roomId, @RequestParam int position) {
        try {
            return userRoomService.prohibit_microphones(roomId, position);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        }
    }

    //    踢人
    @RequestMapping("/kick_out")
    @ResponseBody
    public Response getOutRoom(@RequestParam String uid, @RequestParam String roomId) {
        Response response;
        try {
            response = userRoomService.getOutRoom(roomId, uid, roomId);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

    /**
     * 赠送礼物，并变化魅力值  赠送者id（从taken中拿）
     *
     * @param roomId    房间id
     * @param acceptIds 接受者id集合
     * @param giftId    礼物id
     * @param gift_num  礼物数量（为赠送给一位用户的数量）
     */
    @RequestMapping("/giveGift")
    @ResponseBody
    public Response giveGift(@RequestParam(required = false) String roomId, @RequestParam String[] acceptIds, @RequestParam String giftId,
                             @RequestParam int gift_num, HttpServletRequest request) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        String giver_id;
        if (jwt != null) {
            try {
                List<GiftOrder> list = new ArrayList<>();
                giver_id = jwt.getClaim("user_id").asString();
                if (acceptIds.length > 0) {
                    for (int i = 0; i < acceptIds.length; i++) {
                        GiftOrder giftOrder = new GiftOrder();
                        giftOrder.setGiver_id(giver_id);
                        if (roomId != null && roomId.length() > 0) {
                            giftOrder.setRoom_id(roomId);
                        }
                        giftOrder.setGift_id(giftId);
                        giftOrder.setGift_num(gift_num);
                        giftOrder.setAccept_id(acceptIds[i]);
                        list.add(giftOrder);
                    }
                    response = userRoomService.giveGift(list, roomId, giftId, giver_id, acceptIds);
                } else {
                    response = new Response("抱歉!找不到赠送对象", null, 400);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = new Response("系统出错，请稍后重试", null, 400);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }

    //上传图片
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadImage(MultipartFile image) {
        //上传图片
        //保存数据库的路径
        //定义文件保存的本地路径
//        String localPath = "D:/kol_photo/upload/";
        String localPath = "/usr/local/kol/room_image/";
        //定义 文件名
        String filename = null;
        if (image != null && !image.isEmpty()) {
            System.out.println("imageName=====" + image.getName());
            try {
                //生成uuid作为文件名称
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                //获得文件类型（可以判断如果不是图片，禁止上传）
                String contentType = image.getContentType();
                System.out.println(contentType);
                //获得文件后缀名
                String suffixName = contentType.substring(contentType.indexOf("/") + 1);
                //得到 文件名
                filename = uuid + "." + suffixName;
                //文件保存路径
                image.transferTo(new File(localPath + filename));
                System.out.println(filename);
                return new Response("上传成功", "http://129.204.52.213/room_image/" + filename, 200);
            } catch (Exception e) {
                e.printStackTrace();
                return new Response("系统出错，请稍后重试", null, 400);
            }
        } else {
            return new Response("请上传图片", null, 400);
        }
    }

    //    房间设置的修改
    @RequestMapping(value = "/updateRoom", method = RequestMethod.POST)
    @ResponseBody
    public Response updateRoom(@RequestParam String roomId, @RequestParam(required = false) String room_name, @RequestParam(required = false) String room_topic,
                               @RequestParam(required = false) Integer room_limit, @RequestParam(required = false) Integer room_label,
                               @RequestParam(required = false) String room_notice, @RequestParam(required = false) String room_image) {
        Response response;
        try {
            Map<String, Object> map = new HashMap<>();
            System.out.println("room_notice==" + room_notice);
            if (room_name != null && !room_name.equals("")) {
                map.put("room_name", room_name);
                map.put("room_topic", room_topic);
                map.put("room_notice", room_notice);
            }
            if (room_image != null) {
                map.put("room_image", room_image);
            }
            if (room_limit != null) {
                map.put("room_limit", room_limit);
            }
            if (room_label != null) {
                map.put("room_label", room_label);
            }
            map.put("id", roomId);
            if (userRoomService.updateRoom(map)) {
                response = new Response("修改成功", null, 200);
            } else {
                response = new Response("修改失败", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("修改失败", null, 400);
        }
        return response;
    }

    /**
     * 设置管理员
     *
     * @param uid      用户id
     * @param roomId   房间id
     * @param duration 管理时长 (秒)-1是永久
     * @return
     */
    @RequestMapping("/setAdministrator")
    @ResponseBody
    public Response setAdministrator(String uid, String roomId, String duration) {
        return userRoomService.setAdministrator(uid, roomId, duration);
    }

    /**
     * 获取管理员列表
     *
     * @param roomId 房间id
     * @return
     */
    @RequestMapping("/getAdministrators")
    @ResponseBody
    public Response getAdministrators(String roomId) {
        return userRoomService.getAdministrators(roomId);
    }

    /**
     * 移除管理员
     *
     * @param roomId 房间id
     * @param uid    管理员id
     * @return
     */
    @RequestMapping("/removeAdministrators")
    @ResponseBody
    public Response removeAdministrators(String roomId, String uid) {
        return userRoomService.removeAdministrators(roomId, uid);
    }

    /**
     * 增加管理员时长
     *
     * @param roomId   房间id
     * @param uid      管理员id
     * @param duration 增加时长(秒)
     * @return
     */
    @RequestMapping("/addAdministratorsDuration")
    @ResponseBody
    public Response addAdministratorsDuration(String roomId, String uid, String duration) {
        return userRoomService.addAdministratorsDuration(roomId, uid, duration);
    }

    //打开|关闭 公屏
    @RequestMapping("/publicScreen")
    @ResponseBody
    public Response PublicScreen(String roomId) {
        return userRoomService.Screen(roomId);
    }

    //房间关键字搜索
    @RequestMapping("/searchRoomByKeyWord")
    @ResponseBody
    public Response searchRoomByKeyWord(HttpServletRequest request, String keyWord, String pageNo) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        Integer pageSize = 10;
        try {
            if (jwt != null) {
                //判断关键字是否为null
                System.out.println("keyWord=====" + keyWord);
                String keyWord1 = SLEmojiFilter.filterEmoji(keyWord);
                System.out.println("keyWord1=====" + keyWord1);
                if (keyWord1 != null && keyWord1.length() > 0 && pageNo != null && pageNo.length() > 0) {
                    Integer pN = Integer.valueOf(pageNo);
                    PageHelper.startPage(pN, pageSize);
                    List list = userRoomService.searchRoomByKeyWord(keyWord1);
                    PageInfo<RoomInfos> pageInfo = new PageInfo<RoomInfos>(list);
                    if (pN <= pageInfo.getLastPage()) {
                        response = new Response("模糊查询房间列表成功", JsonUtils.objectToJson(pageInfo.getList()), 200);
                    } else {
                        response = new Response("模糊查询房间列表结束", "[]", 200);
                    }
                } else {
                    response = new Response("缺少必传参数", "[]", 200);
                }
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

}
