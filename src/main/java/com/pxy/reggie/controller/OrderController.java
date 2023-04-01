package com.pxy.reggie.controller;

import com.pxy.reggie.common.R;
import com.pxy.reggie.entity.Orders;
import com.pxy.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-31 16:22
 **/
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }

}
