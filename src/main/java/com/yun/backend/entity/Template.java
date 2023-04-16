package com.yun.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYueyun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Template对象", description = "")
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String templateName;
    private String url;
    private String standard;
    private Integer deleted;
    private Integer creator;
    private String templateImg;
}
