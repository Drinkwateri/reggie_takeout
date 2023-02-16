package com.roy.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roy.reggie_takeout.common.CustomException;
import com.roy.reggie_takeout.entity.Category;
import com.roy.reggie_takeout.entity.Dish;
import com.roy.reggie_takeout.entity.Setmeal;
import com.roy.reggie_takeout.mapper.CategoryMapper;
import com.roy.reggie_takeout.service.CategoryService;
import com.roy.reggie_takeout.service.DishService;
import com.roy.reggie_takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * @description: 根据id删除分类，删除之前进行判断是否关联菜品或套餐
     * @param id
     * @return null
     * @author: Ruofan Li
     * @date: 2023/2/9
     */
    @Override
    public void removeAndCheck(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if(dishCount > 0){
            //已经关联菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        if(setmealCount > 0){
            //已经关联套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
