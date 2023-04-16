package com.yun.backend.ocr;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TrainTicketOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.TrainTicketOCRResponse;
import com.yun.backend.util.ImgBase64Util;
import com.yun.backend.util.Result;
import org.springframework.web.bind.annotation.RequestParam;

public class TrainTicketOCR {
    public static final String SECRET_ID = "AKIDZ0GI5ctv8C4oMjtJfplSvMfleMuEMK4Z";
    public static final String SECRET_KEY = "MQMFyTlrkOxKxISXQ5XlnfPApNkxx61i";

    public static Result<String> getTrainTicketInfo(@RequestParam("path") String path) {
        try {
            // 实例化一个认证对象
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
            TrainTicketOCRRequest req = new TrainTicketOCRRequest();
            req.setImageBase64(imgBase64);
            // 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
            TrainTicketOCRResponse resp = client.TrainTicketOCR(req);
            // 输出json格式的字符串回包
            String result = IDCardOCRResponse.toJsonString(resp);
            return Result.success("识别成功", result);
        } catch (TencentCloudSDKException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
