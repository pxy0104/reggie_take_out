package com.pxy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxy.reggie.dto.SetmealDto;
import com.pxy.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    SetmealDto getByIdWithDishes(Long id);


    void updateWithDishes(SetmealDto setmealDto);

    /**
     * 删除套餐以及他关联的菜品
     */
    void removeWithDish(List<Long> ids);
}
