package com.roy.reggie_takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roy.reggie_takeout.common.R;
import com.roy.reggie_takeout.entity.Category;
import com.roy.reggie_takeout.entity.Employee;
import com.roy.reggie_takeout.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * @description: 新增分类
     * @param category
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/9
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * @description: 分页查询
     * @param page
     * @param pageSize
     * @return com.roy.reggie_takeout.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     * @author: Ruofan Li
     * @date: 2023/2/9
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //进行分页查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * @description: 根据id删除分类
     * @param ids
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/9
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.removeAndCheck(ids);
        return R.success("分类信息删除成功");
    }

    /**
     * @description: 根据id修改分类信息
     * @param category
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/10
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * @description: 根据条件查询分类数据，用于新增菜品功能的菜品分类信息显示
     * @param category
     * @return com.roy.reggie_takeout.common.R<java.util.List<com.roy.reggie_takeout.entity.Category>>
     * @author: Ruofan Li
     * @date: 2023/2/10
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}
