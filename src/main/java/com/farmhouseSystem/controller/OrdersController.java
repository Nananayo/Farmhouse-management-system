package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.common.BaseContext;
import com.farmhouseSystem.entity.Activity;
import com.farmhouseSystem.entity.GuestRoom;
import com.farmhouseSystem.entity.Orders;
import com.farmhouseSystem.entity.User;
import com.farmhouseSystem.service.IActivityService;
import com.farmhouseSystem.service.IGuestRoomService;
import com.farmhouseSystem.service.IOrdersService;
import com.farmhouseSystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private IGuestRoomService guestRoomService;
    @Autowired
    private IActivityService activityService;


    @GetMapping("/{id}")
    @ApiOperation("根据id查询订单")
    public R<Orders> getById(@PathVariable Long id) {
        ordersService.getById(id);
        return R.success(ordersService.getById(id));
    }
    @GetMapping("/userList")
    @ApiOperation("用户查询登录订单")
    public R<List<Orders>> getUserList(HttpSession session) {
        Long id = (Long) session.getAttribute("user");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,id);
        List<Orders> list = ordersService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping("/save")
    @ApiOperation("新增订单")
    public R<Orders> save(@RequestBody Orders orders, HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute("user"));
        if (request.getSession().getAttribute("user") != null) {
            Long id = (Long) request.getSession().getAttribute("user");
            orders.setUserId(id);
            User user = userService.getById(id);
            System.out.println(user);
            System.out.println("用户名:" + user.getName());
            orders.setUserName(user.getName());
            orders.setUserPhone(user.getPhone());
        }

        // 确保 serviceId 和 serviceName 被设置
        if (orders.getServiceId() == null || orders.getServiceName() == null || orders.getServiceImg() == null) {
            return R.error("服务ID和服务名称不能为空");
        }

        ordersService.save(orders);
        log.info("新增订单{}", orders);
        return R.success(orders);
    }
    @PutMapping("/admin/update")
    @ApiOperation("管理员更新订单")
    public R<String> update(@RequestBody Orders orders) {

        ordersService.updateById(orders);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    @ApiOperation("获取订单列表")
    public R<List<Orders>> list(Orders orders) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orders.getUserId() != null,Orders::getUserId,BaseContext.getCurrentId());
        return R.success(ordersService.list(queryWrapper));
    }
    @DeleteMapping("/user")
    @ApiOperation("用户取消订单")
    public R<List<Orders>> deleteByUser(Orders orders) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(orders.getUserId() != null,Orders::getUserId,BaseContext.getCurrentId());
        ordersService.remove(queryWrapper);
        return R.success(ordersService.list(queryWrapper));
    }
    @DeleteMapping("/admin")
    @ApiOperation("管理员删除订单")
    public R<String> deleteByAdmin(long id) {

        ordersService.removeById(id);
        return R.success("删除成功");
    }
    @GetMapping("/page")
    @ApiOperation("分页查询订单")
    public R<Page> getByPage(Page page,Orders orders){
        if(page == null ){
            return R.error("参数错误");
        }
        Page<Orders> pageInfo = new Page(page.getCurrent(),page.getSize());
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if (orders.getNumber() != null){
            queryWrapper.eq(Orders::getNumber,orders.getNumber());
        }
        if (orders.getUserName() != null){
            queryWrapper.like(Orders::getUserName,orders.getUserName());

        }

        ordersService.page(pageInfo,queryWrapper);

        log.info("pageInfo:{}",pageInfo);
        return R.success(pageInfo);
    }
    @GetMapping("/countByServiceId")
    @ApiOperation("根据服务ID查询订单number总和")
    public R<Integer> sumNumberByServiceId(@RequestParam Long serviceId) {

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 2);
        queryWrapper.eq(Orders::getServiceId, serviceId);

        // 获取所有符合条件的订单数据
        List<Orders> ordersList = ordersService.list(queryWrapper);

        // 计算number字段的总和
        int totalNumber = ordersList.stream()
                .mapToInt(Orders::getNumber)
                .sum();
//        if (guestRoomService.getById(serviceId) != null){
//            if (totalNumber == 1){
//                return R.error("民宿已被预订");
//            }
//        }

        return R.success(totalNumber);
    }
    @GetMapping("/activityPeople")
    @ApiOperation("判断活动与民宿是否达到最大人数")
    public R<String> countRemainPeople(Orders orders) {

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 2);
        queryWrapper.eq(Orders::getServiceId, orders.getServiceId());

        // 获取所有符合条件的订单数据
        List<Orders> ordersList = ordersService.list(queryWrapper);

        // 计算number字段的总和
        int totalNumber = ordersList.stream()
                .mapToInt(Orders::getNumber)
                .sum();
        if (activityService.getById(orders.getServiceId()) != null){
            Activity activity = activityService.getById(orders.getServiceId());
            System.out.println("活动人数:"+activity.getMaxNumberPeople());
            System.out.println("订单人数:"+totalNumber);
            int i = orders.getNumber() + totalNumber;
            if (i>activity.getMaxNumberPeople()){
                return R.error("活动人数已满");
            }
        }
        if (guestRoomService.getById(orders.getServiceId()) != null){
            GuestRoom guestRoom = guestRoomService.getById(orders.getServiceId());
            System.out.println("民宿房间:"+guestRoom.getMaxNumberRoom());
            System.out.println("已定房间:"+totalNumber);
            int i = orders.getNumber() + totalNumber;
            if (i>guestRoom.getMaxNumberRoom()){
                return R.error("民宿房间已满");
            }
        }
        return R.success("满足条件");
    }
    @GetMapping("/userRoom")
    @ApiOperation("登录用户已订的民宿")
    public R<List<GuestRoom>> UserRoomList(HttpServletRequest request) {
        Long userId =(Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userId);
        queryWrapper.eq(Orders::getStatus,2);
        List<GuestRoom> guestRoomList = new ArrayList<>();
        for (Orders orders : ordersService.list(queryWrapper)) {
            if (guestRoomService.getById(orders.getServiceId()) != null){
                guestRoomList.add(guestRoomService.getById(orders.getServiceId()));
            }
        }
        return R.success(guestRoomList);
    }
}
