package com.mybatis.demo.controller;

import com.mybatis.demo.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ' 比较使用 Redis 缓存 和 直接查询数据库， 性能上的对比
 * @author lhw
 * @date 2020/10/9
 */
@Slf4j
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 查询库存：通过数据库查询库存
     * @param sid
     * @return
     */
    @RequestMapping("/getStockByDB/{sid}")
    public String getStockByDB(@PathVariable int sid) {
        int count;
        try {
            count = stockService.getStockCountByDB(sid);
        } catch (Exception e) {
            log.error("查询库存失败：[{}]", e.getMessage());
            return "查询库存失败";
        }
        log.info("商品Id: [{}] 剩余库存为: [{}]", sid, count);
        return String.format("商品Id: %d 剩余库存为：%d", sid, count);
    }

    /**
     * 查询库存：通过缓存查询库存
     * 缓存命中：返回库存
     * 缓存未命中：查询数据库写入缓存并返回
     * @param sid
     * @return
     */
    @RequestMapping("/getStockByCache/{sid}")
    public String getStockByCache(@PathVariable int sid) {
        Integer count;
        try {
            count = stockService.getStockCount(sid);
        } catch (Exception e) {
            log.error("查询库存失败：[{}]", e.getMessage());
            return "查询库存失败";
        }
        log.info("商品Id: [{}] 剩余库存为: [{}]", sid, count);
        return String.format("商品Id: %d 剩余库存为：%d", sid, count);
    }

    @RequestMapping("/getStockTestRabbit/{sid}")
    public String getStockTestRabbitMsgDelCache(@PathVariable int sid){
        Integer count = null;
        try {
            count = stockService.getStockCount(sid);
            log.info("【通知消息队列发送消息】getStockTestRabbit");
            rabbitTemplate.convertAndSend("delCache","getStockTestRabbitMsgDelCache: 处理删除缓存");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("商品Id: %d 剩余库存为：%d", sid, count);
    }

}
