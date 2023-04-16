package com.yun.backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.backend.entity.Template;
import com.yun.backend.entity.TemplateVO;
import com.yun.backend.entity.User;
import com.yun.backend.service.ITemplateService;
import com.yun.backend.util.Result;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYueyun
 * @since 2023-03-20
 */
@Slf4j
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Resource
    private ITemplateService templateService;

    public final static Integer FRAME_WIDTH = 680;
    public final static Integer FRAME_HEIGHT = 400;

    @GetMapping("/list")
    public Result<Map<String, Object>> queryTemplateList(@RequestParam(value = "pageNo") Long pageNo,
                                                         @RequestParam(value = "pageSize") Long pageSize) {
        Page<Template> templatePage = new Page<>(pageNo, pageSize);
        templateService.page(templatePage, null);
        Map<String, Object> data = new HashMap<>();
        data.put("total", templatePage.getTotal());
        data.put("rows", templatePage.getRecords());
        return Result.success("数据列表查询成功", data);
    }

    @GetMapping("/{id}")
    public Result<List<Template>> queryTemplateList(@PathVariable Integer id) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(id), Template::getCreator, id);
        List<Template> data = templateService.list(wrapper);
        return Result.success("数据列表查询成功", data);
    }

    @GetMapping("")
    public Result<List<TemplateVO>> queryTemplateByCreator(@RequestParam(value = "creator") Integer creator) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(creator), Template::getCreator, creator).or()
                .eq(Objects.nonNull(creator), Template::getCreator, -1);
        List<Template> templates = templateService.list(wrapper);
        List<TemplateVO> templateVOs = new LinkedList<>();
        for (Template t: templates) {
            templateVOs.add(new TemplateVO(t));
        }
        return Result.success(templateVOs);
    }

    @DeleteMapping("/{id}")
    public Result<Template> deleteTemplateById(@PathVariable("id") Integer id) {
        if (templateService.removeById(id)) {
            return Result.success("数据模板删除成功");
        }
        return Result.badRequest("数据模板删除失败");
    }

    @PostMapping("")
    public Result<?> addTemplate(@RequestBody Map<String, Object> data) {
        String standard = JSON.toJSONString(data.get("standard"));
        Template template = new Template(null, (String) data.get("templateName"),
                null, standard, 0, (Integer) data.get("creator"),
                (String) data.get("templateImg"));
        JSONObject handledStandard = handleTemplate(template);
        template.setStandard(String.valueOf(handledStandard));
        if (templateService.save(template)) {
            return Result.success("数据模板添加成功");
        }
        return Result.badRequest("数据模板添加失败");
    }



    /**
     * 处理模板图片数据与图片实际属性相对应
     */
    private JSONObject handleTemplate(Template originalTemplate) {
        File picture = new File(originalTemplate.getTemplateImg());
        JSONObject originStandard = JSONObject.parseObject(originalTemplate.getStandard().trim());
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(new FileInputStream(picture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sourceImg != null;
        int imgWidth = sourceImg.getWidth();        // 图片宽度
        int imgHeight = sourceImg.getHeight();      // 图片高度
        // 得整个循环 每次调方法handleImageAttribution给实际的替换掉
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : originStandard.entrySet()) {
            JSONObject attrObj = (JSONObject) entry.getValue();
            int x = attrObj.getIntValue("x");
            int y = attrObj.getIntValue("y");
            int w = attrObj.getIntValue("w");
            int h = attrObj.getIntValue("h");
            Map<String, Integer> newAttrs = handleImageAttribution(imgWidth, imgHeight, x, y, w, h);
            attrObj.put("x", newAttrs.get("x"));
            attrObj.put("y", newAttrs.get("y"));
            attrObj.put("w", newAttrs.get("w"));
            attrObj.put("h", newAttrs.get("h"));
            map.put(entry.getKey(), attrObj);
        }
        return new JSONObject(map);
    }

    private Map<String, Integer> handleImageAttribution(int imgWidth, int imgHeight, int x, int y, int w, int h) {
        double trueW = (double) w / FRAME_WIDTH * imgWidth;
        double trueH = (double) h / FRAME_HEIGHT * imgHeight;
        double trueX = (double) x / FRAME_WIDTH * imgWidth;
        double trueY = (double) y / FRAME_HEIGHT * imgHeight;
        Map<String, Integer> trueAttribution = new HashMap<>();
        trueAttribution.put("x", (int) trueX);
        trueAttribution.put("y", (int) trueY);
        trueAttribution.put("w", (int) trueW);
        trueAttribution.put("h", (int) trueH);
        return trueAttribution;
    }
}
