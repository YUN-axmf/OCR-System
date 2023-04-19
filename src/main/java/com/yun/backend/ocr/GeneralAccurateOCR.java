package com.yun.backend.ocr;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRResponse;
import com.yun.backend.util.ImgBase64Util;
import com.yun.backend.util.Result;
import org.springframework.web.bind.annotation.RequestParam;

public class GeneralAccurateOCR {
    public static final String SECRET_ID = "";
    public static final String SECRET_KEY = "";

    public static Result<String> getGeneral(@RequestParam("path") String path) {
        try {
            String imgBase64 = ImgBase64Util.LocalImgToBase64(path);
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, "ap-shanghai", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();
            req.setImageBase64(imgBase64);
            // 返回的resp是一个DriverLicenseOCRResponse的实例，与请求对象对应
            GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);
            // 输出json格式的字符串回包
            String result = GeneralAccurateOCRResponse.toJsonString(resp);
            return Result.success("识别成功", result);
        } catch (TencentCloudSDKException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
