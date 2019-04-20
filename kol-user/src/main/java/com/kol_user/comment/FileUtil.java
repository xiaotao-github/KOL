package com.kol_user.comment;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Component
public class FileUtil {
    //    文件上传
    public String upload(MultipartFile file) throws IOException {
        String filename=null;
        //定义文件保存的本地路径
        String localPath = "D:/upload/";
        //生成uuid作为文件名称
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //获得文件类型（可以判断如果不是图片，禁止上传）
        String contentType = file.getContentType();
        //获得文件后缀名
        String suffixName = contentType.substring(contentType.indexOf("/") + 1);
        //得到 文件名
        filename =uuid + "." + suffixName;
        System.out.println(filename);
        //文件保存路径
        file.transferTo(new java.io.File(localPath + filename));
        return filename;
    }
    //    文件下载,需要测试
    public void download(@RequestParam(value="filename") String filename,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        //模拟文件，myfile.txt为需要下载的文件
        String path = "D:/upload/"+filename;
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new java.io.File(path)));
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename,"UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }
}
