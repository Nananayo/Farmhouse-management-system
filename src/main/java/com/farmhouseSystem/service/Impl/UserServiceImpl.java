package com.farmhouseSystem.service.Impl;

import com.farmhouseSystem.entity.User;
import com.farmhouseSystem.mapper.UserMapper;
import com.farmhouseSystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-10-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
