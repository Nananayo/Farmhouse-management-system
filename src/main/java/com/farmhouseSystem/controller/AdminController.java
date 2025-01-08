package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.entity.Admin;
import com.farmhouseSystem.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-11-15
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理员接口")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @PostMapping("/login")
    @ApiOperation("管理员登入")
    public R<String> login(HttpServletRequest request, @RequestBody Admin admin, HttpSession session) {

        String password = admin.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, admin.getUsername());
        admin = adminService.getOne(queryWrapper);
        if(admin == null){
            return R.error("用户名不存在");
        }
        if(!admin.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if(admin.getStatus() ==0){
            return R.error("账号已禁用");
        }
        request.getSession().setAttribute("admin", admin.getId());

        log.info("登录成功...");
            ServletContext sc = request.getServletContext();
            sc.setAttribute(session.getId(),session);

        return R.success(session.getId());
    }
    @PostMapping("/logout")
    @ApiOperation("管理员登出")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("admin");
        return R.success("退出成功");
    }


}
