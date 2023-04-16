package com.yun.backend.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "TemplateVO对象", description = "将Template对象standard字段转为JSON格式返回给前端")
public class TemplateVO {
    private Integer id;
    private String templateName;
    private JSONObject standard;

    public TemplateVO(Template template) {
        this.id = template.getId();
        this.templateName = template.getTemplateName();
        this.standard = JSONObject.parseObject(template.getStandard());
    }
}
