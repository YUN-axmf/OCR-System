package com.yun.backend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yun.backend.ocr.IDCardOCR;
import com.yun.backend.ocr.TemplateOCR;
import com.yun.backend.util.ImgBase64Util;
import com.yun.backend.util.Result;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class BackendApplicationTests {

    public final static Integer FRAME_WIDTH = 680;
    public final static Integer FRAME_HEIGHT = 400;

    @Test
    void contextLoads() {
    }

    // 完美
    @Test
    void handleImageAttribution() {
        String path = "E:\\YUN\\Studying\\CS\\GraduationProject\\code\\vue-admin-template\\src\\assets\\images\\1.png";
        int x = 185;
        int y = 185;
        int w = 65;
        int h = 71;
        File picture = new File(path);
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(new FileInputStream(picture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sourceImg != null;
        int imgWidth = sourceImg.getWidth();        // 图片宽度
        int imgHeight = sourceImg.getHeight();      // 图片高度
        double trueW = (double) w / FRAME_WIDTH * imgWidth;
        double trueH = (double) h / FRAME_HEIGHT * imgHeight;
        double trueX = (double) x / FRAME_WIDTH * imgWidth;
        double trueY = (double) y / FRAME_HEIGHT * imgHeight;
        System.out.println("x" + (int) trueX);
        System.out.println("y" + (int) trueY);
        System.out.println("w" + (int) trueW);
        System.out.println("h" + (int) trueH);
    }

    @Test
    void testJSON() {
        String jsonStr = "{\"name\":{\"description\":\"名字\",\"x\":10,\"y\":10,\"w\":50,\"h\":100},\"sex\":{\"description\":\"性别\",\"x\":50,\"y\":50,\"w\":200,\"h\":100},\"adress\":{\"description\":\"住址\",\"x\":100,\"y\":200,\"w\":200,\"h\":198}}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        System.out.println(jsonObject);
    }

    @Test
    void testOpenCV() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
    }

    @Test
    void testProcess() {
        JSONObject standard = JSON.parseObject("{\"first\":{\"w\":69,\"x\":317,\"h\":65,\"description\":\"姓\",\"y\":157},\"second\":{\"w\":69,\"x\":416,\"h\":65,\"description\":\"名\",\"y\":157}}");
        String path = "C:\\Users\\96215\\Desktop\\img\\test1.png";
        Map<String, String> map = TemplateOCR.getTemplateInfo(path, standard);
        log.debug(String.valueOf(map));
    }

    @Test
    void testFile() {
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        log.debug("临时文件夹路径: " + tempDirectoryPath);
    }
}
