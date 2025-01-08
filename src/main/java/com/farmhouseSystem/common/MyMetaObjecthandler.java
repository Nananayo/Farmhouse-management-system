package com.farmhouseSystem.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 检查并设置 createUser 和 updateUser
        if (metaObject.hasGetter("createUser")) {
            setFieldValByName("createUser", BaseContext.getCurrentId(), metaObject);
        }
        if (metaObject.hasGetter("updateUser")) {
            setFieldValByName("updateUser", BaseContext.getCurrentId(), metaObject);
        }

        log.info("公共字段自动填充[insert]完成");
        log.info("公共字段自动填充[insert]...");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 检查并设置 updateUser
        if (metaObject.hasGetter("updateUser")) {
            setFieldValByName("updateUser", BaseContext.getCurrentId(), metaObject);
        }

        log.info("公共字段自动填充[update]完成");
    }
}
