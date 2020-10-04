package com.aop.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *   AOP 的实现方式： ① java API ② Java 注解 ③ xml 配置文件
 *   aop切面编程是一种编程思想： 使用aop编程的好处
 *      1.使用aop编程可以降低代码的耦合度
 *      2.提高代码的复用性
 *      3.使系统更加易于扩展
 *
 */
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }

}
