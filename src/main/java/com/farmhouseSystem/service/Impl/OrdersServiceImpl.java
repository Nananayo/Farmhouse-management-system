package com.farmhouseSystem.service.Impl;

import com.farmhouseSystem.entity.Orders;
import com.farmhouseSystem.mapper.OrdersMapper;
import com.farmhouseSystem.service.IOrdersService;
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
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
