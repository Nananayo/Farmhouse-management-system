package com.farmhouseSystem.controller;

import com.farmhouseSystem.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
@Api(tags = "文件上传下载接口")
public class CommonController {
    @Value("${travel.path}")
    private String basePath;

    @PostMapping("/upload")
    @ApiOperation("上传")
    public R<String> upload(MultipartFile file){
        //原始文件名获取后缀
        String originFileName = file.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        //使用UUID生成
        String fileName = UUID.randomUUID().toString()+suffix;

        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }
    @GetMapping("/download")
    @ApiOperation("下载")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
