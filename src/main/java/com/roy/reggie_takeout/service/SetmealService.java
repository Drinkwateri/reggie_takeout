package com.roy.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roy.reggie_takeout.dto.SetmealDto;
import com.roy.reggie_takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * @description: 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     * @return void
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * @description: 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     * @return void
     * @author: Ruofan Li
     * @date: 2023/2/13
     */
    public void removeWithDish(List<Long> ids);
}
