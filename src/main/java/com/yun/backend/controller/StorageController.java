package com.yun.backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.backend.entity.Storage;
import com.yun.backend.entity.User;
import com.yun.backend.service.IStorageService;
import com.yun.backend.service.ITemplateService;
import com.yun.backend.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYueyun
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/storage")
@Slf4j
public class StorageController {
    @Resource
    private IStorageService storageService;

    @Resource
    private ITemplateService templateService;

    @GetMapping("/list")
    public Result<Map<String, Object>> queryStorageList(@RequestParam(value = "user", required = false) Integer user,
                                                        @RequestParam(value = "data", required = false) Integer data,
                                                        @RequestParam(value = "pageNo") Long pageNo,
                                                        @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<Storage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(user), Storage::getUser, user);
        wrapper.eq(Objects.nonNull(data), Storage::getData, data);
        Page<Storage> storagePage = new Page<>(pageNo, pageSize);
        storageService.page(storagePage, wrapper);
        Map<String, Object> values = new HashMap<>();
        values.put("total", storagePage.getTotal());
        values.put("rows", storagePage.getRecords());
        return Result.success("存储列表查询成功", values);
    }

    @PostMapping("")
    public Result<?> addStorage(@RequestBody Map<String, Object> data) {
        String content = JSON.toJSONString(data.get("content"));
        if (content.equals("{}")) {
            return Result.badRequest("数据不存在");
        }
        Storage storage = new Storage(null, (Integer) data.get("user"),
                (Integer) data.get("data"), content, "无", 0, (String) data.get("imgPath"));
        if (storageService.save(storage)) {
            return Result.success("存储成功");
        }
        return Result.badRequest("存储失败");
    }

    @DeleteMapping("/{id}")
    public Result<User> deleteStorageById(@PathVariable("id") Integer id) {
        if (storageService.removeById(id)) {
            return Result.success("存储数据删除成功");
        }
        return Result.badRequest("存储数据删除失败");
    }

    @GetMapping("/{id}")
    public Result<Storage> queryStorageById(@PathVariable("id") Integer id) {
        Storage storage = storageService.getById(id);
        if (storage != null) {
            return Result.success("查询存储数据失败", storage);
        }
        return Result.badRequest("查询存储数据失败");
    }

    @GetMapping("/content/{id}")
    public Result<JSONObject> queryStorageContentById(@PathVariable("id") Integer id) {
        Storage storage = storageService.getById(id);
        JSONObject content = JSONObject.parseObject(storage.getContent());
        if (content != null) {
            JSONObject template = JSONObject.parseObject(templateService.getById(storage.getData()).getStandard());
            JSONObject modifiedJsonObject = new JSONObject();
            for (String key : content.keySet()) {
                String description = template.getJSONObject(key).getString("description");
                Object value = content.get(key);
                modifiedJsonObject.put(description, value);
            }
            return Result.success("查询存储数据成功", modifiedJsonObject);
        }
        return Result.badRequest("查询存储数据失败");
    }

    @GetMapping("/img/{id}")
    public Result<String> queryStorageImgById(@PathVariable("id") Integer id) {
        Storage storage = storageService.getById(id);
        File tempFile = new File(storage.getImgPath());
        String fileName = tempFile.getName();
        return Result.success("查询存储图片成功", fileName);
    }


    @PutMapping("")
    public Result<?> modifyStorage(@RequestBody Storage storage) {
        if (storageService.updateById(storage)) {
            return Result.success("修改备注成功");
        }
        return Result.badRequest("修改备注失败");
    }
}
