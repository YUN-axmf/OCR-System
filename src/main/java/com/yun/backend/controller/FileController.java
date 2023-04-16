package com.yun.backend.controller;

import com.yun.backend.entity.Image;
import com.yun.backend.service.IImageService;
import com.yun.backend.util.ImgBase64Util;
import com.yun.backend.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class FileController {
    @Resource
    private IImageService imageService;

    private final String FILE_PATH = "E:\\YUN\\Studying\\CS\\GraduationProject\\code\\vue-admin-template\\src\\assets\\images\\";

    @PostMapping("/upload")
    public Result<Object> upload(MultipartFile file) throws IOException {
        // 获取文件的原始名称
        //System.out.println(file.getOriginalFilename());
        // 获取Web服务器运行目录
        //String path = request.getServletContext().getRealPath("/upload/");
        String path = FILE_PATH + file.getOriginalFilename();
        saveFile(file, path);
        String imgBase64 = ImgBase64Util.LocalImgToBase64(path);
        Image image = new Image(null, 0, null, path);
        imageService.save(image);
        return Result.success("图片上传成功", path);
    }

    @GetMapping(value = "/download")
    public void downloadFile(String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        String file = path.substring(path.lastIndexOf("\\")+1);
        FileInputStream fis = new FileInputStream(path);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream out = response.getOutputStream();
        byte[] bt = new byte[1024];
        int length = 0;
        while((length=fis.read(bt)) != -1){
            out.write(bt, 0, length);
        }
        out.close();
    }

    public void saveFile(MultipartFile file, String path) throws IOException {
        // 判断存储的目录是否存在 如果不存在则创建
        File dir = new File(path);
        if(!dir.exists()){
        // 创建目录
            dir.mkdir();
        }
        // 创建文件
        File contract = new File(path);
        file.transferTo(contract);
    }
}
