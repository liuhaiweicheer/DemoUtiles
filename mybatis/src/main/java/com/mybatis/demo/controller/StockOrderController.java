package com.mybatis.demo.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.entity.StockOrder;
import com.mybatis.demo.service.StockOrderService;
import com.mybatis.demo.service.StockService;
import com.mybatis.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author lhw
 * @date 2020/10/5
 */
@Slf4j
@RestController
@RequestMapping("/stock")
public class StockOrderController {

    @Autowired
    StockOrderService stockOrderService;

    @Autowired
    UserService userService;

//     令牌桶 每秒放行 10 个请求
    RateLimiter rateLimiter = RateLimiter.create(10);

    @GetMapping("/creatOrder")
    public StockOrder createOrder(@RequestParam("sId")Integer sId, @RequestParam("size")Integer size){
        StockOrder order = null;
        try {
            order = stockOrderService.createOrder(sId, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @GetMapping("/creatOrderByRedis")
    public StockOrder createOrderByRedisLock(@RequestParam("sId")Integer sId, @RequestParam("size")Integer size){
        StockOrder order = null;
        try {
            order = stockOrderService.createOrderByRedisLock(sId, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @GetMapping("/creatOrderByRateLimiter")
    public StockOrder creatOrderByRateLimiter(@RequestParam("sId")Integer sId, @RequestParam("size")Integer size){
//        阻塞式获取令牌
//        rateLimiter.acquire()
//    非阻塞式获取令牌
        if(!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)){
            log.info("被限流了，访问失败。。。。请重试");
            throw new RuntimeException("被限流了，访问失败。。。。请重试");
        }
        StockOrder order = null;
        try {
            order = stockOrderService.createOrderByRedisLock(sId, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @GetMapping("/creatOrderByPessimisticOrder")
    public StockOrder creatOrderByPessimisticOrder(@RequestParam("sId")Integer sId, @RequestParam("size")Integer size){
        StockOrder stockOrder = null;
        try {
            stockOrder = stockOrderService.createOrderByPessimistic(sId, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockOrder;

    }

    /**
     * 要求验证的抢购接口
     * @param sid
     * @return
     */
    @RequestMapping(value = "/createOrderWithVerifiedUrl", method = {RequestMethod.GET})
    public String createOrderWithVerifiedUrl(@RequestParam(value = "sid") Integer sid,
                                             @RequestParam(value = "userId") Integer userId,
                                             @RequestParam(value = "verifyHash") String verifyHash) {
        int stockLeft;
        try {
            stockLeft = stockOrderService.createVerifiedOrder(sid, userId, verifyHash);
            log.info("购买成功，剩余库存为: [{}]", stockLeft);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return e.getMessage();
        }
        return String.format("购买成功，剩余库存为：%d", stockLeft);
    }


    /**
     * 要求验证的抢购接口 + 单用户限制访问频率
     * @param sid
     * @return
     */
    @RequestMapping(value = "/createOrderWithVerifiedUrlAndLimit", method = {RequestMethod.GET})
    @ResponseBody
    public String createOrderWithVerifiedUrlAndLimit(@RequestParam(value = "sid") Integer sid,
                                                     @RequestParam(value = "userId") Integer userId,
                                                     @RequestParam(value = "verifyHash") String verifyHash) {
        int stockLeft;
        try {
            int count = userService.addUserCount(userId);
            log.info("用户截至该次的访问次数为: [{}]", count);
            boolean isBanned = userService.getUserIsBanned(userId);
            if (isBanned) {
                return "购买失败，超过频率限制";
            }
            stockLeft = stockOrderService.createVerifiedOrder(sid, userId, verifyHash);
            log.info("购买成功，剩余库存为: [{}]", stockLeft);
        } catch (Exception e) {
            log.error("购买失败：[{}]", e.getMessage());
            return e.getMessage();
        }
        return String.format("购买成功，剩余库存为：%d", stockLeft);
    }


    @RequestMapping("/test")
    public String test(){
        return "hello";
    }

}
