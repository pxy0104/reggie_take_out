package com.pxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pxy.reggie.common.R;
import com.pxy.reggie.entity.Employee;
import com.pxy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-23 16:08
 **/

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录模块
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        //页面提交的密码进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //与数据库中的用户username比较查询
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);
        //比较username 对应的用户是否存在
        if (emp == null) {
            return R.error("登录失败");
        }
        //比较密码
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //检查员工账号是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //可以登录
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        log.info("{}",employee.toString());  //测试封装employee
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long editUserId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(editUserId);
//        employee.setUpdateUser(editUserId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工列表分页查询
     * @param page 当前页
     * @param pageSize 一页大小
     * @param name 页面查询员工姓名
     * @return pages 返回页所有页信息
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},page={},name={}",page,pageSize,name);//测试接口是否接收到数据
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        //过滤 模糊查询
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        Page pages = employeeService.page(pageInfo, wrapper);
        return R.success(pages);
    }
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        log.info(employee.toString()); //测试接口是否接收参数成功
        Long updateUserId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(updateUserId);
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 编辑数据回显
     * @param id 前端请求id
     * @return 数据库员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }

}
