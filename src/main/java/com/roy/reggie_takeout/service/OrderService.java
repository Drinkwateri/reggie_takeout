package com.roy.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roy.reggie_takeout.entity.Orders;
import com.roy.reggie_takeout.mapper.OrderMapper;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
