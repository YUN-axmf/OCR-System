package com.yun.backend.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ImgBase64Util {

    /**
     * 将本地图片转成 base64 字符串
     * @param filePath 本地文件的位置
     * @return
     */
    public static String LocalImgToBase64(String filePath) {
        String base64_prefix = "data:image/png;base64,";
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        ByteArrayOutputStream out = null;
        try {
            FileInputStream in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int i = 0;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, b.length);
            }
            out.close();
            in.close();
            return base64_prefix + Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
