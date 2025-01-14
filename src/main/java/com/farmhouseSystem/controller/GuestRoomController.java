package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.entity.Activity;
import com.farmhouseSystem.entity.GuestRoom;
import com.farmhouseSystem.service.IGuestRoomService;
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
@RequestMapping("/guest-room")
@Slf4j
@Api(tags = "民宿管理接口")
public class GuestRoomController {
    @Autowired
    private IGuestRoomService guestRoomService;
    @GetMapping("/{id}")
    @ApiOperation("根据id查询民宿")
    public R<GuestRoom> getById(@PathVariable("id") Long id) {
        GuestRoom guestRoom = guestRoomService.getById(id);
        if (guestRoom == null) {
            return R.error("民宿不存在");
        }
        return R.success(guestRoom);
    }
    @GetMapping("/page")
    @ApiOperation("分页查询民宿")
    public R<Page> getByPage(Page page, String name){
        if (name == null){
            Page<GuestRoom> pageInfo = new Page(page.getCurrent(),page.getSize());
            LambdaQueryWrapper<GuestRoom> queryWrapper = new LambdaQueryWrapper<>();
            guestRoomService.page(pageInfo,queryWrapper);
            return R.success(pageInfo);
        }

        if(page == null ){
            return R.error("参数错误");
        }
        Page<GuestRoom> pageInfo = new Page(page.getCurrent(),page.getSize());
        LambdaQueryWrapper<GuestRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(GuestRoom::getName,name);
        guestRoomService.page(pageInfo,queryWrapper);

        log.info("pageInfo:{}",pageInfo);
        return R.success(pageInfo);
    }
    @GetMapping("/list")
    @ApiOperation("查询民宿列表")
    private R<List<GuestRoom>> list(GuestRoom attractions){
        LambdaQueryWrapper<GuestRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(attractions.getStatus()!=null,GuestRoom::getId,attractions.getId());
        List<GuestRoom> list = guestRoomService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping
    @ApiOperation("新增民宿")
    public R<GuestRoom> save(@RequestBody GuestRoom attractions){
        if(attractions.getPhone() == null){
            return R.error("电话为空");
        }
        if(attractions.getName() == null){
            return R.error("名字为空为空");
        }
        guestRoomService.save(attractions);
        log.info(attractions.toString());
        return R.success(attractions);
    }
    @DeleteMapping("/{id}")
    @ApiOperation("删除民宿")
    public R<String> delete(@PathVariable("id") Long id){
        guestRoomService.removeById(id);
        return R.success("删除成功");
    }
    @DeleteMapping("/ids")
    @ApiOperation("批量删除民宿")
    public R<String> delete(@RequestParam("ids") List<Long> ids){

        System.out.println(ids);

        if (ids == null){
            return R.error("ids为空");
        }
        for (Long id : ids) {
            LambdaQueryWrapper<GuestRoom> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(GuestRoom::getId, id);
            guestRoomService.remove(queryWrapper1);
            System.out.println(id);
        }

        return R.success("删除成功");
    }
    @PutMapping
    @ApiOperation("更新民宿信息")
    public R<String> update(@RequestBody GuestRoom attractions){
        if(attractions.getId() == null){
            return R.error("id为空");
        }
        guestRoomService.updateById(attractions);
        return R.success("修改成功");
    }

}
