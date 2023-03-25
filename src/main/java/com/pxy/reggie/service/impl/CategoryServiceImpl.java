package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.common.CustomException;
import com.pxy.reggie.entity.Category;
import com.pxy.reggie.entity.Dish;
import com.pxy.reggie.entity.Setmeal;
import com.pxy.reggie.mapper.CategoryMapper;
import com.pxy.reggie.service.CategoryService;
import com.pxy.reggie.service.DishService;
import com.pxy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-25 15:23
 **/

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setMealService;

    /**
     * 根据id删除当前分类，判断是否关联套餐
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Dish::getCategoryId, id);
        int count = dishService.count(wrapper1);
        //判断分类中是否有餐
        if (count > 0) {
//            TODO 已经关联菜品，抛出业务异常
            throw new CustomException("分类中含有菜品,不能删除！");
        }
        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Setmeal::getCategoryId, id);
        int count1 = setMealService.count(wrapper2);
        if (count1>0) {
            //套餐分类中含有菜品不能删除
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        //空的分类，可以删除
        super.removeById(id);
    }
}
