package com.pxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pxy.reggie.common.R;
import com.pxy.reggie.dto.DishDto;
import com.pxy.reggie.dto.SetmealDto;
import com.pxy.reggie.entity.Category;
import com.pxy.reggie.entity.Dish;
import com.pxy.reggie.entity.Setmeal;
import com.pxy.reggie.entity.SetmealDish;
import com.pxy.reggie.service.CategoryService;
import com.pxy.reggie.service.SetmealDishService;
import com.pxy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-27 18:06
 **/

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;



    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息={}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageParam = new Page(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        wrapper.like(name!=null,Setmeal::getName,name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);

        Page<Setmeal> pageInfo = setmealService.page(pageParam, wrapper);
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
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
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }
    //修改套餐数据回显部分
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDishes(id);
        return R.success(setmealDto);
    }
    //保存套餐修改内容
    @PutMapping
    public R<String> update(@RequestBody SetmealDto  setmealDto) {
        setmealService.updateWithDishes(setmealDto);
        return R.success("修改成功");
    }
    //修改套餐的售卖状态
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {
//            log.info(ids.toString());
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            if (setmeal.getStatus() != status) {
                setmeal.setStatus(status);
            }
            setmealService.updateById(setmeal);
        }
        return R.success("售卖状态修改成功");
    }

    //删除套餐/批量删除套餐
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    public R<List<Setmeal>> list(@RequestBody Setmeal setmeal){

        return null;
    }
}
