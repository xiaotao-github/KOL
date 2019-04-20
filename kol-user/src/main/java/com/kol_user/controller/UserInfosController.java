package com.kol_user.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kol_user.comment.JsonUtils;
import com.kol_user.comment.TokenUtil;
import com.kol_user.dto.*;
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
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserInfosController {
    @Autowired
    UserService userService;


    //获取用户关注、粉丝、是否有新礼物
    @RequestMapping("/getUserInfos")
    @ResponseBody
    public Response getUserInfos(HttpServletRequest request) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String user_id = jwt.getClaim("user_id").asString();
            String userInfos = userService.getUserInfos(user_id);
            if (userInfos != null && !userInfos.equals("")) {
                response = new Response("用户基本信息获取成功", userInfos, 200);
            } else {
                response = new Response("系统出错，请稍后重试", null, 400);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }


    //房间关键字搜索
    @RequestMapping("/searchUserByKeyWord")
    @ResponseBody
    public Response searchUserByKeyWord(HttpServletRequest request, String keyWord, String pageNo) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        Integer pageSize = 10;
        try {
            if (jwt != null) {
                //判断关键字是否为null
                if (keyWord != null && keyWord.length() != 0 && pageNo != null && pageNo.length() > 0) {
                    Integer pN = Integer.valueOf(pageNo);
                    Page<Object> page = PageHelper.startPage(pN, pageSize);
                    List list = userService.searchUserByKeyWord(keyWord);
                    PageInfo<UserInfos> pageInfo = new PageInfo<UserInfos>(list);
                    if (pN <= page.getPages()) {
                        response = new Response("模糊查询用户列表成功", JsonUtils.objectToJson(pageInfo.getList()), 200);
                    } else {
                        response = new Response("模糊查询用户列表结束", "[]", 200);
                    }
                } else {
                    response = new Response("缺少必传参数", "[]", 400);
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


    //获取用户信息
    @RequestMapping("/getUserInfosById")
    @ResponseBody
    public Response getUserInfosById(HttpServletRequest request, String uid) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        Map map = new HashMap();
        try {
            if (jwt != null) {
                User user = userService.findOneById(uid);
                if (user != null && user.getId() != null) {
                    map.put("uid", user.getId());
                    map.put("name", user.getUsername());
                    map.put("avatar", user.getUser_image());
                    map.put("signature", user.getIntroduction());
                    response = new Response("查询用户基本信息成功", JsonUtils.objectToJson(map), 200);
                } else {
                    response = new Response("用户不存在", null, 402);
                }
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
        } catch (Exception e) {
            response = new Response("系统错误，请稍后重试", null, 400);
        }
        return response;
    }

    /***
     * 好友主页展示
     * @param request
     * @param targetUser_id
     * @return
     */
    @RequestMapping("/visitUserPage")
    @ResponseBody
    public Response visitUserPage(HttpServletRequest request, String targetUser_id) {
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            if (jwt != null) {
                try {
                    String user_id = jwt.getClaim("user_id").asString();
                    String result = userService.visitUserPage(user_id, targetUser_id);
                    if (!result.equals("系统出错")){
                        response = new Response("好友展示成功", result, 200);
                    }else {
                        response = new Response("系统错误，请稍后重试", null, 400);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = new Response("系统错误，请稍后重试", null, 400);
                }
            } else {
                response = new Response("登录信息失效，请重新登录", null, 405);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统错误，请稍后重试", null, 400);
            return response;
        }
    }


    //获取用户个人资料（）
    @RequestMapping("/getUserInfosByUser")
    @ResponseBody
    public Response getUserInfosByUser(HttpServletRequest request, String uid) {
        Response response;
        String token = request.getHeader("token");
        DecodedJWT jwt = TokenUtil.parseJWT(token);
        if (jwt != null) {
            String user_id = jwt.getClaim("user_id").asString();
            User user = userService.findOneById(uid);
            if (user != null && user.getId() != null) {
                String userInfos = userService.getUserInfosByUser(user_id, uid);
                if (userInfos != null && !userInfos.equals("")) {
                    response = new Response("用户基本信息获取成功", userInfos, 200);
                } else {
                    response = new Response("系统出错，请稍后重试", null, 400);
                }
            } else {
                response = new Response("用户不存在", null, 400);
            }
        } else {
            response = new Response("登录信息失效，请重新登录", null, 405);
        }
        return response;
    }


    //新增举报信息
    @RequestMapping("/addReportInformation")
    @ResponseBody
    public Response addReportInformation(HttpServletRequest request,
                                         @RequestParam(required = false) String uid, @RequestParam(required = false) String type,
                                         @RequestParam(required = false) String describe, @RequestParam(required = false) String image1,
                                         @RequestParam(required = false) String image2, @RequestParam(required = false) String image3,
                                         @RequestParam(required = false) String telephone, @RequestParam(required = false) String QQ) {
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            if (jwt != null) {
                String user_id = jwt.getClaim("user_id").asString();
                ReportInformation reportInformation = new ReportInformation();
                // reportInformation.setId();
                reportInformation.setUid(user_id);
                if (uid != null) {
                    reportInformation.setReport_id(uid.trim());
                }
                if (type != null) {
                    reportInformation.setType(type.trim());
                }
                if (describe != null) {
                    reportInformation.setDescribe(describe.trim());
                }
                if (image1 != null) {
                    reportInformation.setImage1(image1.trim());
                }
                if (image2 != null) {
                    reportInformation.setImage2(image2.trim());
                }
                if (image3 != null) {
                    reportInformation.setImage3(image3.trim());
                }
                if (telephone != null) {
                    reportInformation.setTelephone(telephone.trim());
                }
                if (QQ != null) {
                    reportInformation.setQQ(QQ.trim());
                }
                boolean b = userService.addReportInformation(reportInformation);
                if (b) {
                    response = new Response("举报成功", null, 200);
                } else {
                    response = new Response("举报失败", null, 400);
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

    //上传图片（多图片上传）
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadImage(MultipartFile[] images) {
        //上传图片
        //保存数据库的路径
        //定义文件保存的本地路径
//        String localPath = "D:/kol_photo/upload/";
        String localPath = "/usr/local/kol/report_image/";
        //定义 文件名
        String filename;
        try {
            if (images != null && images.length > 0) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < images.length; i++) {
                    System.out.println("imageName=====" + images[i].getName());
                    //生成uuid作为文件名称
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    //获得文件类型（可以判断如果不是图片，禁止上传）
                    String contentType = images[i].getContentType();
                    System.out.println(contentType);
                    //获得文件后缀名
                    String suffixName = contentType.substring(contentType.indexOf("/") + 1);
                    //得到 文件名
                    filename = uuid + "." + suffixName;
                    //文件保存路径
                    images[i].transferTo(new File(localPath + filename));
                    System.out.println(filename);
                    list.add("http://129.204.52.213/report_image/" + filename);
                }
                return new Response("上传成功", list, 200);
            } else {
                return new Response("请上传图片", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("系统出错，请稍后重试", null, 400);
        }
    }

    //新增用户备注
    @RequestMapping(value = "/addRemark", method = RequestMethod.POST)
    @ResponseBody
    public Response addRemark(HttpServletRequest request, String uid, String remark) {
        Response response;
        try {
            Remark remark1 = new Remark();
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            String user_id = jwt.getClaim("user_id").asString();
            remark1.setUser_id(user_id);
            remark1.setUid(uid);
            remark1.setRemark(remark);
            userService.addRemark(remark1);
            response = new Response("新增备注成功", null, 200);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

    //查询用户备注
    @RequestMapping(value = "/searchUserRemark", method = RequestMethod.POST)
    @ResponseBody
    public Response searchUserRemark(HttpServletRequest request, String uid) {
        Response response;
        try {
            String token = request.getHeader("token");
            DecodedJWT jwt = TokenUtil.parseJWT(token);
            String user_id = jwt.getClaim("user_id").asString();
            Remark remark = userService.searchUserRemark(user_id, uid);
            if (remark != null && remark.getUid() != null) {
                response = new Response("获取用户备注成功", remark.getRemark(), 200);
            } else {
                response = new Response("用户不存在", null, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response("系统出错，请稍后重试", null, 400);
        }
        return response;
    }

}
