package com.roy.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roy.reggie_takeout.entity.DishFlavor;
import com.roy.reggie_takeout.mapper.DishFlavorMapper;
import com.roy.reggie_takeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
