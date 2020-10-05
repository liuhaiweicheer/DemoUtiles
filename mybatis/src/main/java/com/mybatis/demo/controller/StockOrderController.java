package com.mybatis.demo.controller;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.entity.StockOrder;
import com.mybatis.demo.service.StockOrderService;
import com.mybatis.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhw
 * @date 2020/10/5
 */
@RestController
@RequestMapping("/stock")
public class StockOrderController {

    @Autowired
    StockOrderService stockOrderService;

    @GetMapping("/creatOrder")
    public StockOrder createOrder(@RequestParam("sId")Integer sId, @RequestParam("size")Integer size){
        StockOrder order = stockOrderService.createOrder(sId, size);
        return order;
    }

    @GetMapping("/creatOrderByRedis")
    public StockOrder createOrderByRedisLock(@RequestParam("sId")Integer sId, @RequestParam("size")Integer size){
        StockOrder order = stockOrderService.createOrderByRedisLock(sId, size);
        return order;
    }

    @RequestMapping("/test")
    public String test(){
        return "hello";
    }

}
