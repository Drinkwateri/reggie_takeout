package com.roy.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roy.reggie_takeout.dto.DishDto;
import com.roy.reggie_takeout.entity.Dish;
import com.roy.reggie_takeout.entity.DishFlavor;
import com.roy.reggie_takeout.mapper.DishMapper;
import com.roy.reggie_takeout.service.DishFlavorService;
import com.roy.reggie_takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * @description: 新增菜品，同时保存对应的口味数据
     * @param dishDto
     * @return void
     * @author: Ruofan Li
     * @date: 2023/2/10
     */
    @Override
    @Transactional
    public void savaWithFlavor(DishDto dishDto) {
        //保存菜品的基本数据到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId(); //菜品Id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());

        //保存菜品口味信息到菜品口味表dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    /**
     * @description: 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return com.roy.reggie_takeout.dto.DishDto
     * @author: Ruofan Li
     * @date: 2023/2/11
     */
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        /**
         * 更新dish表
         */
        this.updateById(dishDto);

        /**
         * 更新flavor表
         */
        //1. 清理当前菜品对应口味数据----dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //2. 添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }
}
