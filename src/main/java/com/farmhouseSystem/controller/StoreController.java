package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.entity.Store;
import com.farmhouseSystem.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-12
 */
@RestController
@RequestMapping("/store")
@Slf4j
@Api(tags = "总店管理接口")
public class StoreController {
    @Autowired
    private IStoreService storeService;

    @GetMapping
    @ApiOperation("获取店铺信息")
    public R<Store> getStoreInfo() {
        Store store = storeService.getById("1846081228545410561");
        return R.success(store);
    }

    @PutMapping
    @ApiOperation("修改店铺信息")
    public R<String> updateStoreInfo(@RequestBody Store store) {
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getId, store.getId());
        boolean updated = storeService.update(store,queryWrapper);
        if (updated) {
            return R.success("店铺信息修改成功");
        }
        return R.error("店铺信息修改失败");
    }


}
