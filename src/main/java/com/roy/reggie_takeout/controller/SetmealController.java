package com.roy.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roy.reggie_takeout.common.R;
import com.roy.reggie_takeout.dto.SetmealDto;
import com.roy.reggie_takeout.entity.Category;
import com.roy.reggie_takeout.entity.Setmeal;
import com.roy.reggie_takeout.service.CategoryService;
import com.roy.reggie_takeout.service.SetmealDishService;
import com.roy.reggie_takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @description: 新增套餐
     * @param setmealDto
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * @description: 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return com.roy.reggie_takeout.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * @description: 删除套餐
     * @param ids
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){ //基本数据类型不用，引用数据类型需要加注解
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    /**
     * @description: 根据条件查询套餐数据
     * @param setmeal
     * @return com.roy.reggie_takeout.common.R<java.util.List<com.roy.reggie_takeout.entity.Setmeal>>
     * @author: Ruofan Li
     * @date: 2023/2/15
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        return R.success(setmealList);
    }
}
