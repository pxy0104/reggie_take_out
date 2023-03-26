package com.pxy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxy.reggie.dto.DishDto;
import com.pxy.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
     void saveWithFlavor(DishDto dishDto);
}
