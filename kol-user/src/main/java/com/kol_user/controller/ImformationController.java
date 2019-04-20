package com.kol_user.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.kol_user.comment.FileUtil;
import com.kol_user.comment.TokenUtil;
import com.kol_user.dto.Remark;
import com.kol_user.dto.Response;
import com.kol_user.dto.Room;
import com.kol_user.dto.User;
import com.kol_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@Controller
@RequestMapping("/Update")
public class ImformationController {
    @Autowired
    UserService userService;
    @Autowired
    FileUtil file;


    /***
     * 修改个人信息
     * @param request
     * @param user_image
     * @param username
     * @param introduction
     * @param height
     * @param age
     * @param hobby
     * @param position
     * @return
     */
    @RequestMapping(value = "/updateImformation", method = {RequestMethod.POST})
    @ResponseBody
    public Response updateImformation(HttpServletRequest request, @RequestParam(required = false) Integer sex, @RequestParam(required = false) String user_image,
                                      @RequestParam(required = false) String username, @RequestParam(required = false) String introduction,
                                      @RequestParam(required = false) String height, @RequestParam(required = false) String age,
                                      @RequestParam(required = false) String hobby, @RequestParam(required = false) String position) {
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            if (jwt != null) {
                String user_id = jwt.getClaim("user_id").asString();
                User user = userService.findOneById(user_id);
                if (sex!=null && sex!=user.getSex()) {
                    //查询性别是否已经修改过
                    Integer time = userService.checkSexTime(user_id);
                    //次数小于等于1，可以修改性别
                    if (time<=1){
                        if (sex==2){
                            user.setSex(sex);
                            user.setUser_image("http://129.204.52.213/user_image/ic_woman_square.png");
                        }else {
                            user.setSex(sex);
                        }
                    }else if (time==2){
                        response = new Response("性别修改次数已达上限", null, 401);
                        return response;
                    }else {
                        response = new Response("系统错误，请稍后重试", null, 400);
                        return response;
                    }
                }
                if (user_image != null) {
                    user.setUser_image(user_image);
                }
                if (username != null) {
                    user.setUsername(username);
                }
                if (introduction != null) {
                    user.setIntroduction(introduction);
                }
                if (height != null) {
                    user.setHeight(height);
                }
                if (age != null) {
                    user.setAge(age);
                }
                if (hobby != null) {
                    user.setHobby(hobby);
                }
                if (position != null) {
                    user.setPosition(position);
                }
                boolean isSuccess = userService.updateImformation(user);
                if (isSuccess) {
                    if (username != null && !username.equals("")){
                        Room room = userService.findRoomById(user_id);
                        if(room!=null) {
                            room.setUsername(username);
                            userService.updateRoomUsername(room);
                        }
                        response = new Response("修改个人信息成功", null, 200);
                        return response;
                    }else {
                        response = new Response("修改个人信息成功", null, 200);
                        return response;
                    }
                } else {
                    response = new Response("修改个人信息失败", null, 305);
                    return response;
                }
            }else {
                response = new Response("登录信息失效，请重新登录", null, 405);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试", null, 400);
            return response;
        }
    }


    //上传图片
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadImage(MultipartFile image){
        //上传图片
        //保存数据库的路径
        //定义文件保存的本地路径
//        String localPath = "D:/kol_photo/upload/";
        String localPath = "/usr/local/kol/user_image/";
        //定义 文件名
        String filename = null;
        if (image != null && !image.isEmpty()) {
            System.out.println("imageName====="+image.getName());
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
                return new Response("上传成功","http://129.204.52.213/user_image/" + filename,200);
            } catch (Exception e) {
                e.printStackTrace();
                return new Response("系统出错，请稍后重试", null, 400);
            }
        }else{
            return new Response("请上传图片", null, 400);
        }
    }

    //修改用户备注
    @RequestMapping(value = "/updateRemark", method = RequestMethod.POST)
    @ResponseBody
    public Response updateRemark(HttpServletRequest request,String uid,String remark) {
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            String user_id = jwt.getClaim("user_id").asString();
            Remark remark1 = userService.searchUserRemark(user_id, uid);
            if (remark1!=null && remark1.getUid()!=null){
                remark1.setRemark(remark);
                boolean b = userService.updateRemark(remark1);
                if (b){
                    response = new Response("修改用户备注成功",null, 200);
                }else {
                    response = new Response("修改用户备注失败", null, 400);
                }
            }else {
                response = new Response("用户不存在", null, 400);
            }
        }catch (Exception e){
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }
}