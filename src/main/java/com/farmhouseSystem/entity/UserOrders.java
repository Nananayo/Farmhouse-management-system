package com.farmhouseSystem.entity;

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
@TableName("user_orders")
public class UserOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户-订单id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long ordersId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 路线
     */
    private String route;

    /**
     * 分店名
     */
    private String branchName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
