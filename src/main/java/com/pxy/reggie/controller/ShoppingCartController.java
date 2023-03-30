package com.pxy.reggie.controller;

import com.pxy.reggie.common.R;
import com.pxy.reggie.entity.ShoppingCart;
import com.pxy.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


        return null;
    }

}
