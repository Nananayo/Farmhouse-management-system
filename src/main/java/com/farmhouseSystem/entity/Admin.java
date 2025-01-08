package com.farmhouseSystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 高权限用户id
     */
    @TableId(value = "id", type = IdType.UUID)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录用密码
     */
    private String password;

    /**
     * 权限
     */
    private Integer power;
    private Integer status;


}
