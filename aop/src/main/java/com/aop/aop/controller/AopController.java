package com.aop.aop.controller;

import com.aop.aop.exception.AopException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhw
 * @date 2020/10/3
 */

@RestController
@RequestMapping("/aop")
public class AopController {

    @RequestMapping("/exec")
    public String testException(@RequestParam("id") Integer id){
        if(id.equals(1)){
            throw new AopException("id 为 1");
        }
        return "hello";
    }

}
