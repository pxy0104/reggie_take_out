package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.entity.ShoppingCart;
import com.pxy.reggie.mapper.ShoppingCartMapper;
import com.pxy.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-30 22:45
 **/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
