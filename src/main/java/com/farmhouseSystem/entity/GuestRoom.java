package com.farmhouseSystem.entity;

import java.math.BigDecimal;
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
 * @since 2025-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("guest_room")
@ApiModel(value="GuestRoom对象", description="")
public class GuestRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "景点id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String img;

    private String name;

    private String phone;

    private BigDecimal price;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "景点类型")
    private String type;

    private Integer maxNumberRoom;

    private String desces;

    private Integer area;

    @ApiModelProperty(value = "省级名称")
    private String provinceName;

    @ApiModelProperty(value = "市级名称")
    private String cityName;

    @ApiModelProperty(value = "区级名称")
    private String districtName;


}
