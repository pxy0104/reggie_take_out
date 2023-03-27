package com.pxy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxy.reggie.dto.DishDto;
import com.pxy.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
     void saveWithFlavor(DishDto dishDto);
     DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteByIds(List<Long> ids);
}
