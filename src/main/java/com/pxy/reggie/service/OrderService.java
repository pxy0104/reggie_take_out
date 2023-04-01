package com.pxy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxy.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
