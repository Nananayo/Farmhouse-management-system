package com.farmhouseSystem.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */

    private Integer number;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 路线名
     */
    private String routeName;

    /**
     * 分店名
     */
    private String branchName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 路线id
     */
    private Long routeId;

    /**
     * 路线
     */
    private String line;

    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     */
    private Integer status;

    /**
     * 地区
     */
    private String region;

    /**
     * 价钱
     */
    private BigDecimal price;

    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 结账时间
     */

    private LocalDateTime checkoutTime;

    /**
     * 备注
     */
    private String description;


}
