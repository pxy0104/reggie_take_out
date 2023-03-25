package com.pxy.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-25 11:51
 **/

/**
 * 自定义元数据对象处理器
 */
@Component
@Slf4j
public class MymetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充");
        Long empId = BaseContext.getCurrentId();//在ThreadLocal中获取当前登录用户id
//        Long editUserId = (Long) request.getSession().getAttribute("employee");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",empId);
        metaObject.setValue("updateUser",empId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long empId = BaseContext.getCurrentId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",empId);
    }
}
