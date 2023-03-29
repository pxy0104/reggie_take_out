package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.entity.AddressBook;
import com.pxy.reggie.mapper.AddressBookMapper;
import com.pxy.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-29 20:01
 **/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
