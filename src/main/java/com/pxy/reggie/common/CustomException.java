package com.pxy.reggie.common;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-25 17:43
 **/

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
