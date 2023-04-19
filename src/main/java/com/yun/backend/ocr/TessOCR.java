package com.yun.backend.ocr;

import com.yun.backend.util.Result;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class TessOCR {
    public static Result<String> getInfo(String path) {
        File imageFile = new File(path);
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        instance.setDatapath("D:\\tessdata"); // path to tessdata directory
        instance.setLanguage("chi_sim");
        try {
            String result = instance.doOCR(imageFile);
            return Result.success("识别成功", result);
        } catch (TesseractException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
