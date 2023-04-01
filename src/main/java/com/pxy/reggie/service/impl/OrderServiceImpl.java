package com.pxy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxy.reggie.common.BaseContext;
import com.pxy.reggie.common.CustomException;
import com.pxy.reggie.entity.*;
import com.pxy.reggie.mapper.OrderMapper;
import com.pxy.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-31 16:19
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户提交订单
     *
     * @param orders 前端返回的订单信息
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        //获得当前用户id
        Long currentId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空,无需下单");
        }

        //查询用户数据
        User user = userService.getById(currentId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址信息有误,不能下单");
        }

        long number = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(number);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //想订单表插入数据,一条数据
        //1.设置用户id
        orders.setUserId(currentId);
        //2.订单号
        orders.setNumber(String.valueOf(number));
        //3.order time
        orders.setOrderTime(LocalDateTime.now());
        //order checkout time
        orders.setCheckoutTime(LocalDateTime.now());
        //status
        orders.setStatus(2);
        //id
        orders.setId(number);
        //amount
        orders.setAmount(new BigDecimal(amount.get()));
        //userName
        orders.setUserName(user.getName());
        //consignee
        orders.setConsignee(addressBook.getConsignee());
        //phone
        orders.setPhone(addressBook.getPhone());
        //address book
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);

        //项目订单详情表插入数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(wrapper);
    }
}
