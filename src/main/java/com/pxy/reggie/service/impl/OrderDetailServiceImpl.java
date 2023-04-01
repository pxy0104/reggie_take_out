package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.entity.Employee;
import com.pxy.reggie.entity.OrderDetail;
import com.pxy.reggie.mapper.EmployeeMapper;
import com.pxy.reggie.mapper.OrderDetailMapper;
import com.pxy.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-31 16:11
 **/

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
