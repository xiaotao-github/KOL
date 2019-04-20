package com.kol_friends.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

@Component
@Data
public class Message {
    //序号
    int id;
    //文章标题
    String title;
    //副标题
    String subhead;
    //位置
//    int position;
    //链接
    String url;
    //发布时间
    Date time;
    //发布人
    String editor;
    //状态
    int state;
    //内容
    String content;
    //图片
    String image;
    MultipartFile file;
}
