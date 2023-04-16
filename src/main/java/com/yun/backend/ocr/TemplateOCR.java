package com.yun.backend.ocr;

import com.alibaba.fastjson.JSONObject;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 针对用户自定义的模板进行识别
 */
public class TemplateOCR {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static Map<String, String> getTemplateInfo(String path, JSONObject standard) {
        File imageFile = new File(path);
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("D:\\tessdata");
        tesseract.setLanguage("chi_sim");

        // 预处理图像并将结果保存为临时文件
        File preprocessedImageFile = preprocess(imageFile);

        BufferedImage image = null;
        try {
            image = ImageIO.read(preprocessedImageFile);
//            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 解析standard对象，获取每个文本区域的位置信息，并将其存储在一个Map中
        Map<String, Rectangle> regions = new HashMap<>();
        for (Map.Entry<String, Object> entry : standard.entrySet()) {
            JSONObject regionJson = (JSONObject) entry.getValue();
            int x = regionJson.getInteger("x");
            int y = regionJson.getInteger("y");
            int w = regionJson.getInteger("w");
            int h = regionJson.getInteger("h");
            regions.put(entry.getKey(), new Rectangle(x, y, w, h));
        }

        // 识别图像中每个文本区域的文本，并将结果存储在一个Map中
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Rectangle> entry : regions.entrySet()) {
            Rectangle region = entry.getValue();
            assert image != null;
            BufferedImage subimage = image.getSubimage(region.x, region.y,
                    region.width, region.height);
            String text = null;
            try {
                text = tesseract.doOCR(subimage).trim();
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            result.put(entry.getKey(), text);
        }
        return result;
    }

    public static File preprocess(File imageFile) {
        // 读取图片
        Mat inputImage = Imgcodecs.imread(imageFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
        // 二值化
        Mat binaryImage = new Mat();
        Imgproc.threshold(inputImage, binaryImage, 0, 255,
                Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        // 降噪
        Mat denoisedImage = new Mat();
        Imgproc.medianBlur(binaryImage, denoisedImage, 3);
        // 保存预处理后的图片到临时文件
        File tempFile = null;
        try {
            tempFile = File.createTempFile("preprocessed_", ".jpg");
            Imgcodecs.imwrite(tempFile.getAbsolutePath(), denoisedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }
}

