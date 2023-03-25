package com.pxy.reggie.common;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-25 14:46
 **/

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal= new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
       return threadLocal.get();
    }
}
