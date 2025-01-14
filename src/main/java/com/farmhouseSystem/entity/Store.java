package com.farmhouseSystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store")
@ApiModel(value="Store对象", description="")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分店id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Integer status;

    @ApiModelProperty(value = "省级名称")
    private String provinceName;

    @ApiModelProperty(value = "市级名称")
    private String cityName;

    @ApiModelProperty(value = "区级名称")
    private String districtName;

    private String phone;


}
