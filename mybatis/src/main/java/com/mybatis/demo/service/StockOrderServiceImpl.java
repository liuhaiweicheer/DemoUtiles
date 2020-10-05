package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.entity.StockOrder;
import com.mybatis.demo.mapper.StockOrderMapper;
import com.mybatis.demo.redis.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

/**
 * @author lhw
 * @date 2020/10/5
 */
@Slf4j
@Service
public class StockOrderServiceImpl implements StockOrderService{

    private static final int TIMEOUT = 10 * 1000; // 超时时间 10 s

    @Autowired
    StockOrderMapper stockOrderMapper;

    @Autowired
    StockService stockService;

    @Autowired
    RedisLock redisLock;

    /**
     *  在并发条件下 会导致 订单数量创建异常
     *  解决办法： 对 库存数据进行加锁，  方法一、 在数据库中加上一个版本号，进行版本控制，在更新库存的时候进行乐观锁控制
     *                                  方法二、 使用 redis 对并发进行控制
     * @param sId
     * @param size
     * @return
     */
    public StockOrder createOrder(int sId, int size){
        // 检查库存
        Stock stock = stockService.checkStock(sId);
        int cnt = stock.getCount() - stock.getSale();
        if(cnt < size){
            throw new RuntimeException("库存不足");
        }
        stock.setSale(stock.getSale()+size);
        //刚更新库存
//        stockService.saleStock(stock);
        stockService.saleStockOptimistic(stock);
        // 创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(sId);
        stockOrder.setName(stock.getName());
        stockOrderMapper.insert(stockOrder);

        return stockOrder;
    }

    public StockOrder createOrderByRedisLock(int sId, int size){
        //  加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(String.valueOf(sId), String.valueOf(time))){
            throw new RuntimeException("【Redis加锁失败】");
        }
        log.info("【加锁成功】");
        // 检查库存
        Stock stock = stockService.checkStock(sId);
        int cnt = stock.getCount() - stock.getSale();
        if(cnt < size){
            throw new RuntimeException("库存不足");
        }
        stock.setSale(stock.getSale()+size);
        //刚更新库存
        stockService.saleStock(stock);
        // 创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(sId);
        stockOrder.setName(stock.getName());
        stockOrderMapper.insert(stockOrder);

        // 解锁
        redisLock.unlock(String.valueOf(sId), String.valueOf(time));
        return stockOrder;
    }

}
