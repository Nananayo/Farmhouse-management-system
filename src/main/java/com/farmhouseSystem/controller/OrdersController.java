package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.common.BaseContext;
import com.farmhouseSystem.entity.Orders;
import com.farmhouseSystem.entity.User;
import com.farmhouseSystem.service.IOrdersService;
import com.farmhouseSystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-10-08
 */
@RestController
@RequestMapping("/orders")
@Slf4j
@Api(tags = "订单接口")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    @ApiOperation("根据id查询订单")
    public R<Orders> getById(@PathVariable Long id) {
        ordersService.getById(id);
        return R.success(ordersService.getById(id));
    }
    @GetMapping("/userlist")
    @ApiOperation("查询登录用户订单")
    public R<List<Orders>> getUserList() {
        Long id =BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,id);
        List<Orders> list = ordersService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping
    @ApiOperation("新增订单")
    public R<String> save(@RequestBody Orders orders, HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute("user"));
        if (request.getSession().getAttribute("user") !=null){
            Long id =(Long) request.getSession().getAttribute("user");
            orders.setUserId(id);
            User user = userService.getById(id);
            System.out.println(user);
            System.out.println("用户名:"+user.getName());
            orders.setUserName(user.getName());
        }
//        request.getSession().getAttribute("user");
//        System.out.println("session信息:"+request.getSession().getAttribute("user"));
        System.out.println("用户id:"+BaseContext.getCurrentId());
//        System.out.println("用户id:"+orders.setUserId(BaseContext.getCurrentId()));
//        if (orders.getRouteId() == null){
//            return R.error("请选择旅游线路");
//        }
//        List<Integer> list = new ArrayList<>();
//        Collections.addAll(list,1,2,3,4);
//        if (list.contains(orders.getStatus())&&orders.getStatus() != null){
//            orders.setUserId(userService.getById(BaseContext.getCurrentId()).getId());
            ordersService.save(orders);

            log.info("新增订单{}",orders);
            return R.success("新增成功");
//        }
//        return R.error("参数错误");
    }
    @PutMapping("/update")
    @ApiOperation("更新订单")
    public R<String> update(@RequestBody Orders orders) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,BaseContext.getCurrentId());
        Orders orders1 = ordersService.getOne(queryWrapper);
        orders1.setStatus(orders.getStatus());
        if (orders.getStatus() ==2){
            orders1.setCheckoutTime(LocalDateTime.now());
        }
        ordersService.updateById(orders1);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    @ApiOperation("获取订单列表")
    public R<List<Orders>> list(Orders orders) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orders.getUserId() != null,Orders::getUserId,BaseContext.getCurrentId());
        return R.success(ordersService.list(queryWrapper));
    }
    @DeleteMapping
    @ApiOperation("删除订单")
    public R<List<Orders>> delete(Orders orders) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orders.getUserId() != null,Orders::getUserId,BaseContext.getCurrentId());
        ordersService.remove(queryWrapper);
        return R.success(ordersService.list(queryWrapper));
    }
    @GetMapping("/page")
    @ApiOperation("分页查询订单")
    public R<Page> getByPage(Page page,Orders orders){
        if(page == null ){
            return R.error("参数错误");
        }
        if (orders.getId() == null && orders.getRouteName() == null){
            Page<Orders> pageInfo = new Page(page.getCurrent(),page.getSize());
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            ordersService.page(pageInfo,queryWrapper);
            return R.success(pageInfo);
        }
        if (orders.getRouteName() != null){
            Page<Orders> pageInfo = new Page(page.getCurrent(),page.getSize());
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Orders::getRouteName,orders.getRouteName());
            ordersService.page(pageInfo,queryWrapper);
            return R.success(pageInfo);
        }


        Page<Orders> pageInfo = new Page(page.getCurrent(),page.getSize());
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Orders::getId,orders.getId());
        ordersService.page(pageInfo,queryWrapper);

        log.info("pageInfo:{}",pageInfo);
        return R.success(pageInfo);
    }
//    @GetMapping("/pageGroup")
//    @ApiOperation("根据路线分页查询分团")
//    public R<Page> getByRoutePage(Page page,String name){
//        if (name == null){
//            Page<Orders> pageInfo = new Page(page.getCurrent(),page.getSize());
//            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.groupBy(Orders::getRouteName,Orders::getId);
//            ordersService.page(pageInfo,queryWrapper);
//            return R.success(pageInfo);
//        }
//
//        if(page == null ){
//            return R.error("参数错误");
//        }
//        Page<Orders> pageInfo = new Page(page.getCurrent(),page.getSize());
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.groupBy(Orders::getRouteName,Orders::getId);
//        queryWrapper.like(Orders::getRouteName,name);
//        ordersService.page(pageInfo,queryWrapper);
//
//        log.info("pageInfo:{}",pageInfo);
//        return R.success(pageInfo);
//    }

}
