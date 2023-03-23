package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.entity.Employee;
import com.pxy.reggie.mapper.EmployeeMapper;
import com.pxy.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-23 16:06
 **/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
