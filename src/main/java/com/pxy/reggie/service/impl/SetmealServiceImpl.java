package com.pxy.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.common.CustomException;
import com.pxy.reggie.dto.SetmealDto;
import com.pxy.reggie.entity.Setmeal;
import com.pxy.reggie.entity.SetmealDish;
import com.pxy.reggie.mapper.SetmealMapper;
import com.pxy.reggie.service.SetmealDishService;
import com.pxy.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-25 17:08
 **/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);

        //保存套餐和菜品的关联信息
        List<SetmealDish> setmealDishes =
                setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜品和套餐的关联信息至setmeal_dish表中
        setmealDishService.saveBatch(setmealDishes);
    }

    //套餐数据回显
    @Override
    public SetmealDto getByIdWithDishes(Long id) {
        //查询套餐基本信息，从setmeal表查询
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        //查询当前套餐对应的菜品信息，从setmeal_dish表查询
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(dishes);
        return setmealDto;
    }

    //保存套餐修改数据
    @Transactional
    @Override
    public void updateWithDishes(SetmealDto setmealDto) {
        //更新setmeal表的基本信息
        this.updateById(setmealDto);
        //清理当前套餐对应的菜品数据---setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //添加当前提交过来的菜品数据---setmeal_dish表的insert操作
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item) -> {
//            item.setId(IdWorker.getId());
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    /**
     * 删除套餐以及他关联的菜品
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //根据ids获取套餐状态
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        wrapper.in(Setmeal::getId, ids);
        wrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(wrapper);
        //若不可以删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐售卖中，无法删除");
        }
        //如果可以删除 删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(wrapper1);
    }

}
