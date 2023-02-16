package com.roy.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roy.reggie_takeout.entity.ShoppingCart;
import com.roy.reggie_takeout.mapper.ShoppingCartMapper;
import com.roy.reggie_takeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
