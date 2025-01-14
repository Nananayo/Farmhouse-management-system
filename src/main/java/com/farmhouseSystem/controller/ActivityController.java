package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.entity.Activity;
import com.farmhouseSystem.entity.Delicacies;
import com.farmhouseSystem.service.IActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-10
 */
@RestController
@RequestMapping("/activity")
@Slf4j
@Api(tags = "活动管理接口")
public class ActivityController {
    @Autowired
    private IActivityService activitiesService;
    @GetMapping("/{id}")
    @ApiOperation("根据id查询活动")
    public R<Activity> getById(@PathVariable("id") Long id) {
        Activity activity = activitiesService.getById(id);
        if (activity == null) {
            return R.error("活动不存在");
        }
        return R.success(activity);
    }
    @GetMapping("/page")
    @ApiOperation("分页查询活动")
    public R<Page> getByPage(Page page, String name){
        if (name == null){
            Page<Activity> pageInfo = new Page(page.getCurrent(),page.getSize());
            LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
            activitiesService.page(pageInfo,queryWrapper);
            return R.success(pageInfo);
        }

        if(page == null ){
            return R.error("参数错误");
        }
        Page<Activity> pageInfo = new Page(page.getCurrent(),page.getSize());
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Activity::getName,name);
        activitiesService.page(pageInfo,queryWrapper);

        log.info("pageInfo:{}",pageInfo);
        return R.success(pageInfo);
    }
    @GetMapping("/list")
    @ApiOperation("查询活动列表")
    private R<List<Activity>> list(Activity attractions){
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(attractions.getStatus()!=null,Activity::getId,attractions.getId());
        List<Activity> list = activitiesService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping
    @ApiOperation("新增活动")
    public R<Activity> save(@RequestBody Activity attractions){
        if(attractions.getPhone() == null){
            return R.error("电话为空");
        }
        if(attractions.getName() == null){
            return R.error("名字为空为空");
        }
        activitiesService.save(attractions);
        log.info(attractions.toString());
        return R.success(attractions);
    }
    @DeleteMapping("/{id}")
    @ApiOperation("删除活动")
    public R<String> delete(@PathVariable("id") Long id){
        activitiesService.removeById(id);
        return R.success("删除成功");
    }
    @DeleteMapping("/ids")
    @ApiOperation("批量删除活动")
    public R<String> delete(@RequestParam("ids") List<Long> ids){

        System.out.println(ids);

        if (ids == null){
            return R.error("ids为空");
        }
        for (Long id : ids) {
            LambdaQueryWrapper<Activity> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Activity::getId, id);
            activitiesService.remove(queryWrapper1);
            System.out.println(id);
        }

        return R.success("删除成功");
    }
    @PutMapping
    @ApiOperation("更新活动信息")
    public R<String> update(@RequestBody Activity attractions){
        if(attractions.getId() == null){
            return R.error("id为空");
        }
        if(attractions.getPhone() == null){
            return R.error("电话为空");
        }
        if(attractions.getName() == null){
            return R.error("姓名为空");
        }

        activitiesService.updateById(attractions);
        return R.success("修改成功");
    }

}
