// package com.xigua.controller;
//
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.multipart.MultipartFile;
//
// import java.io.File;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Random;
//
// @Controller
// @RequestMapping("/upload")
// public class UploadController {
//     @RequestMapping("/image")
//     @ResponseBody
//     public Map<String,String> upload(@RequestParam("file") MultipartFile file){
//         Map<String,String> result = new HashMap<>();
//         if(file.isEmpty()){
//             result.put("code","1");
//             result.put("msg","文件为空");
//         }
//         String fileName = file.getOriginalFilename();
//         int size = (int) file.getSize();
//         System.out.println(fileName +"-->" +size);
//
//         String path = "D:/kol-photo/upload";
//         File dest = new File(path+"/"+fileName);
//         if (!dest.getParentFile().exists()){//判断文件父目录是否存在
//             //不存在则创建一个目录
//             dest.getParentFile().mkdir();
//         }
//         try {
//             file.transferTo(dest);//保存文件
//             result.put("code","0");
//             result.put("msg","上传成功");
//             result.put("url",dest.getPath());
//         } catch (IOException e) {
//             e.printStackTrace();
//             result.put("code","1");
//             result.put("msg","上传失败");
//         }catch (IllegalStateException e) {
//             e.printStackTrace();
//             result.put("code","1");
//             result.put("msg","上传失败");
//         }
//         return result;
//     }
//
//     public static String uploadFile(MultipartFile file,String path) throws IOException {
//         String name = file.getOriginalFilename();//上传文件的真是名称
//         String suffixName = name.substring(name.lastIndexOf("."));//获取后缀名
//         String hash = Integer.toHexString(new Random().nextInt());//自定义随机数（字母+数字）作为文件名
//         String fileName = hash+suffixName;
//         File temFile = new File(path,fileName);
//         if (!temFile.getParentFile().exists()){
//             temFile.getParentFile().mkdir();
//         }
//         if (temFile.exists()){
//             temFile.delete();
//         }
//         temFile.createNewFile();
//         file.transferTo(temFile);
//         return temFile.getName();
//     }
//
// }
