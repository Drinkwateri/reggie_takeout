package com.roy.reggie_takeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @description: 全局异常处理
 * @author: Ruofan Li
 * @date: 2023/2/7
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class}) //添加了@RestController和@Controller的都会被本处理器处理
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * @description: 异常处理方法
     * @param ex
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/7
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = "用户" + split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * @description: 自定义异常处理方法
     * @param ex
     * @return com.roy.reggie_takeout.common.R<java.lang.String>
     * @author: Ruofan Li
     * @date: 2023/2/9
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
