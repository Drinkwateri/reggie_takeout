package com.roy.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roy.reggie_takeout.entity.Category;

public interface CategoryService extends IService<Category> {
    public void removeAndCheck(Long id);
}
