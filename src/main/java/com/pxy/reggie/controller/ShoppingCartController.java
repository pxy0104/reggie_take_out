package com.pxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pxy.reggie.common.BaseContext;
import com.pxy.reggie.common.R;
import com.pxy.reggie.entity.ShoppingCart;
import com.pxy.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-30 22:49
 **/
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //表中设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            wrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(wrapper);
        if (cartServiceOne != null) {
            //说明已经存在，只需number+1即可，更新操作
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如果为空的话，就加1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
/*        //设置用户id，指定当前时哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //SQL:select * from shopping_cart where user_id = ? and dish_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne.getNumber() > 1) {
            //如果已经存在，就在原来数量基础上减去一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);
            return R.success(cartServiceOne);
//            cartServiceOne = shoppingCart;
        } else {
            shoppingCartService.remove(queryWrapper);
        }
        return R.success(cartServiceOne);*/
        /**
         * 减少菜品或套餐
         * @param shoppingCart
         * @return
         */
//                    log.info("购物车数据：{}",shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //需要判断菜品或套餐是否在购物车里
        //1.区分减少的是菜品还是套餐
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            //菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //2.查看购物车内该菜品或套餐数量是否大于等于1
        ShoppingCart shopping = shoppingCartService.getOne(queryWrapper);
        if (shopping.getNumber() > 1) {
            shopping.setNumber(shopping.getNumber() - 1);
            shoppingCartService.updateById(shopping);
        } else if (shopping.getNumber() == 1) {
            shopping.setNumber(shopping.getNumber() - 1);
            shoppingCartService.removeById(shopping);
        }
        return R.success(shopping);

    }

    //查看购物车信息
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list();
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(wrapper);
        return R.success("清空购物车成功");
    }
}
