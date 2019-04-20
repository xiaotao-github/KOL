package com.home.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Data
public class Home {
    //内容ID
    String id;
    //内容标题
    String content_title;
    //内容简介
    String introduction;
    //排序
    int number;
    //作品链接
    String works_url;
    //作品封面图
    String works_image;
    //发布人
    String issuer;
    //状态
    int state;

    MultipartFile file;
}
