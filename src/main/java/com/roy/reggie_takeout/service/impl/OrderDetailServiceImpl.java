package com.roy.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roy.reggie_takeout.entity.OrderDetail;
import com.roy.reggie_takeout.mapper.OrderDetailMapper;
import com.roy.reggie_takeout.mapper.OrderMapper;
import com.roy.reggie_takeout.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
