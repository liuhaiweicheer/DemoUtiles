package com.websocket.websocket.controller;


import com.myutis.starter.myutilssptingbootstarterautoconfigurer.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HelloController {

    @Autowired
    HelloService helloService;


    @RequestMapping("/lhw")
    public String hello(){
        return helloService.sayHello("lhw");
    }


    @GetMapping("/hello")
    public String sayHello(){
        return "Hello....";
    }

}
