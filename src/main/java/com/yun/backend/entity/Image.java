package com.yun.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Image对象", description = "")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deleted;

    private String base64;

    private String path;

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", deleted=" + deleted +
            ", base64=" + base64 +
            ", path=" + path +
        "}";
    }
}
