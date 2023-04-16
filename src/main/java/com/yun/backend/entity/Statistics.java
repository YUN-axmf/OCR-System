package com.yun.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
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
 * @since 2023-04-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Statistics对象", description = "")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deleted;

    private Integer user;

    private Integer template;

    @ApiModelProperty("是否成功访问OCR接口")
    private Integer succeed;

    private LocalDate visitDate;

}
