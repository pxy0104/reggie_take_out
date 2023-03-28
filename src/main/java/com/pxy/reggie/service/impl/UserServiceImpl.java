package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.entity.User;
import com.pxy.reggie.mapper.UserMapper;
import com.pxy.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-28 15:06
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
