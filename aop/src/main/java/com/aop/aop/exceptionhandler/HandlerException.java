package com.aop.aop.exceptionhandler;

import com.aop.aop.exception.AopException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要配合@ExceptionHandler使用。
 * 当将异常抛到controller时,可以对异常进行统一处理,规定返回的json格式或是跳转到一个错误页面
 * @author lhw
 * @date 2020/10/3
 */
@Slf4j
@ControllerAdvice
public class HandlerException {


    @ResponseBody
    @ExceptionHandler(value = AopException.class)
    public Map aopException(AopException e){
        log.info("【AopException 异常】");
        Map map = new HashMap();
        map.put("code",400);
        map.put("msg",e.getMsg());
        map.put("message",e.getMessage());
        return map;
    }


}
