package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.entity.Delicacies;
import com.farmhouseSystem.service.IDelicaciesService;
import com.farmhouseSystem.service.IDelicaciesService;
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
@RequestMapping("/delicacies")
@Slf4j
@Api(tags = "农家菜管理接口")
public class DelicaciesController {
    @Autowired
    private IDelicaciesService delicaciesService;
    @GetMapping("/{id}")
    @ApiOperation("根据id查询农家菜")
    public R<Delicacies> getById(@PathVariable("id") Long id) {
        Delicacies delicacies = delicaciesService.getById(id);
        if (delicacies == null) {
            return R.error("农家菜不存在");
        }
        return R.success(delicacies);
    }
    @GetMapping("/page")
    @ApiOperation("分页查询农家菜")
    public R<Page> getByPage(Page page, String name){
        if (name == null){
            Page<Delicacies> pageInfo = new Page(page.getCurrent(),page.getSize());
            LambdaQueryWrapper<Delicacies> queryWrapper = new LambdaQueryWrapper<>();
            delicaciesService.page(pageInfo,queryWrapper);
            return R.success(pageInfo);
        }

        if(page == null ){
            return R.error("参数错误");
        }
        Page<Delicacies> pageInfo = new Page(page.getCurrent(),page.getSize());
        LambdaQueryWrapper<Delicacies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Delicacies::getName,name);
        delicaciesService.page(pageInfo,queryWrapper);

        log.info("pageInfo:{}",pageInfo);
        return R.success(pageInfo);
    }
    @GetMapping("/list")
    @ApiOperation("查询农家菜列表")
    private R<List<Delicacies>> list(Delicacies attractions){
        LambdaQueryWrapper<Delicacies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(attractions.getStatus()!=null,Delicacies::getId,attractions.getId());
        List<Delicacies> list = delicaciesService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping
    @ApiOperation("新增农家菜")
    public R<Delicacies> save(@RequestBody Delicacies delicacies){
        if(delicacies.getName() == null){
            return R.error("名字为空");
        }
        delicaciesService.save(delicacies);
        log.info(delicacies.toString());
        return R.success(delicacies);
    }
    @DeleteMapping("/{id}")
    @ApiOperation("删除农家菜")
    public R<String> delete(@PathVariable("id") Long id){
        delicaciesService.removeById(id);
        return R.success("删除成功");
    }
    @DeleteMapping("/ids")
    @ApiOperation("批量删除农家菜")
    public R<String> delete(@RequestParam("ids") List<Long> ids){

        System.out.println(ids);

        if (ids == null){
            return R.error("ids为空");
        }
        for (Long id : ids) {
            LambdaQueryWrapper<Delicacies> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Delicacies::getId, id);
            delicaciesService.remove(queryWrapper1);
            System.out.println(id);
        }

        return R.success("删除成功");
    }
    @PutMapping
    @ApiOperation("更新农家菜信息")
    public R<String> update(@RequestBody Delicacies attractions){
        if(attractions.getId() == null){
            return R.error("id为空");
        }


        delicaciesService.updateById(attractions);
        return R.success("修改成功");
    }

}
