package com.yun.backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yun.backend.entity.Template;
import com.yun.backend.ocr.*;
import com.yun.backend.service.ITemplateService;
import com.yun.backend.util.Result;
import com.yun.backend.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/identify")
public class IdentifyController {
    @Resource
    private ITemplateService templateService;

    @GetMapping("/idCard")
    public Result<Map<String, String>> idCard(String path) {
        Result<String> result = IDCardOCR.getIDCardInfo(path);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return Result.badRequest(result.getMessage());
        } else {
            Map<String, String> info = JSON.parseObject(result.getData(), Map.class);
            info.remove("AdvancedInfo");
            info.remove("RequestId");
            return Result.success(result.getMessage(), info);
        }
    }

    @GetMapping("/driverLicense")
    public Result<Map<String, String>> driverLicense(String path) {
        Result<String> result = DriverLicenseOCR.getDriverLicense(path);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return Result.badRequest(result.getMessage());
        } else {
            Map<String, String> info = JSON.parseObject(result.getData(), Map.class);
            info.remove("RecognizeWarnMsg");
            info.remove("RequestId");
            info.remove("RecognizeWarnCode");
            info.remove("ArchivesCode");
            info.remove("CumulativeScore");
            info.remove("State");
            info.remove("Record");
            return Result.success(result.getMessage(), info);
        }
    }

    @GetMapping("/general")
    public Result<Map<String, String>> general(String path) {
        Result<String> result = GeneralAccurateOCR.getGeneral(path);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return Result.badRequest(result.getMessage());
        } else {
            Map<String, String> info = JSON.parseObject(result.getData(), Map.class);
            return Result.success(result.getMessage(), info);
        }
    }

    @GetMapping("/tess")
    public Result<String> tess(String path) {
        Result<String> result = TessOCR.getInfo(path);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return Result.badRequest(result.getMessage());
        } else {
            return result;
        }
    }

    @GetMapping("/trainTicket")
    public Result<Map<String, String>> TrainTicket(String path) {
        Result<String> result = TrainTicketOCR.getTrainTicketInfo(path);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return Result.badRequest(result.getMessage());
        } else {
            Map<String, String> info = JSON.parseObject(result.getData(), Map.class);
            info.remove("RequestId");
            info.remove("ReceiptNumber");
            info.remove("OriginalPrice");
            info.remove("AdditionalCost");
            info.remove("HandlingFee");
            info.remove("LegalAmount");
            info.remove("InvoiceType");
            info.remove("InvoiceStyle");
            info.remove("IsReceipt");
            return Result.success(result.getMessage(), info);
        }
    }

    @GetMapping("/vatInvoiceInfo")
    public Result<Map<String, String>> VatInvoiceInfo(String path) {
        Result<String> result = VatInvoiceOCR.getVatInvoiceInfo(path);
        if (result.getCode() != ResultCode.SUCCESS.getCode()) {
            return Result.badRequest(result.getMessage());
        } else {
            Map<String, String> info = JSON.parseObject(result.getData(), Map.class);
            info.remove("RequestId");
            return Result.success(result.getMessage(), info);
        }
    }


    @GetMapping("/userDefined")
    public Result<Map<String, String>> UserDefined(@RequestParam("path") String path,
                                                   @RequestParam("templateID") Integer templateID) {
        Template value = templateService.getById(templateID);
        JSONObject standard = JSONObject.parseObject(value.getStandard());
        Map<String, String> content = TemplateOCR.getTemplateInfo(path, standard);
        return Result.success("数据识别成功", content);
    }
}
