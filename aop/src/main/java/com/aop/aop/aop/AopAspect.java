package com.aop.aop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 *  声明一个切面类
 * @author lhw
 * @date 2020/10/4
 */
@Slf4j
@Aspect
@Component
public class AopAspect {

    @Pointcut("execution(public * com.aop.aop.controller.AopController.*(..)) && args(id)")
    public void verify(int id){

    }

    @Before("verify(id)")
    public void doBeforeAopController(int id){
      log.info("【aopController 执行前业务】"+ id);
    }

    @After("verify(id)")
    public void afterAopController(int id){
        log.info("【aopController 执行后业务】" + id);
    }

    @AfterReturning("verify(id)")
    public void doAfterReturningAopController(int id){
        log.info("【doAfterReturningAopController】" + id);
    }

    @AfterThrowing("verify(id)")
    public void doAfterThrowing(int id){
        log.info("【 doAfterThrowing 】" + id);
    }
    /**
     *  aop 环绕通知
     *  环绕通知可以将你所编写的逻辑将被通知的目标方法完全包装起来
     *  我们可以使用一个环绕通知来代替之前多个不同的前置通知和后置通知
     *
     *  环绕通知接受ProceedingJoinPoint作为参数，它来调用被通知的方法。通知方法中可以做任何的事情，当要将控制权交给被通知的方法时，
     *  需要调用ProceedingJoinPoint的proceed()方法。当你不调用proceed()方法时，将会阻塞被通知方法的访问。
     */
//    @Around("verify(id)")
//    public void doAround(ProceedingJoinPoint proceedingJoinPoint, int id){
//        try {
//            log.info("【方法执行前】"+ id);
//            proceedingJoinPoint.proceed();  // 调用目标方法
//            log.info("【返回通知--方法】");
//
//        } catch (Throwable throwable) {
//            log.info("【发生异常】");
//        }
//
//    }

}
