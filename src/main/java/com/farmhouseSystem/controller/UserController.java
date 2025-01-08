package com.farmhouseSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.farmhouseSystem.common.R;
import com.farmhouseSystem.entity.User;
import com.farmhouseSystem.service.IUserService;
import com.farmhouseSystem.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-10-08
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping
    @ApiOperation("新增用户")
    public R<User> save(@RequestBody User user){
        userService.save(user);
        log.info("新增用户信息："+user);
        return R.success(user);
    }
    @GetMapping("/{id}")
    @ApiOperation("根据用户ID查找")
    public R<User> getById(@PathVariable Long id){
        User user = userService.getById(id);
        return R.success(user);
    }
    @PutMapping
    @ApiOperation("修改用户信息")
    public R<String> update(@RequestBody User user){
        userService.updateById(user);
        return R.success("个人信息修改成功");
    }
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public R<Page> page(Page page,String name){
        Page<User> pageInfo = new  Page(page.getCurrent(),page.getSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,User::getName,name);
        userService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @PostMapping("/sendMsg")
    @ApiOperation("发送验证码")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        if (user.getPhone()!=null){
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        session.setAttribute(user.getPhone(),code);
        log.info("code:{}",code);
        return R.success("发送成功");
        }
        return R.error("发送失败");
    }
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public R<String> login(@RequestBody Map map, HttpSession session,HttpServletRequest request){
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        //从Redis中获取保存的验证码
        //Object codeInSession = redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            session.setAttribute("power",user.getPower());
            ServletContext sc = request.getServletContext();
            sc.setAttribute(session.getId(),session);

            return R.success(session.getId());
        }
        return R.error("登录失败");
    }



}
