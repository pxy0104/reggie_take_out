package com.pxy.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxy.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
   void remove(Long id);
}
