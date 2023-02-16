package com.roy.reggie_takeout.controller;

import com.roy.reggie_takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * @description: 文件上传
     * @param file
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/10
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){ //形参名必须和前端form表单的name保持一致

        //原始文件名
        String originalFilename = file.getOriginalFilename();

        //获取原始文件名的后缀 -> .jpg
        String[] split = originalFilename.split("\\.");
        String suffix = split[1];

        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + "." + suffix;

        //创建一个目录对象，防止目录不存在的情况
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，创建
            dir.mkdirs();
        }

        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //返回文件名称
        return R.success(fileName);
    }

    /**
     * @description: 文件下载
     * @param name
     * @param response
     * @return void
     * @author: Ruofan Li
     * @date: 2023/2/10
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流。通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流。通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
