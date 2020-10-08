package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.entity.StockOrder;
import com.mybatis.demo.entity.User;
import com.mybatis.demo.mapper.StockOrderMapper;
import com.mybatis.demo.mapper.UserMapper;
import com.mybatis.demo.redis.RedisLock;
import com.mybatis.demo.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    StringRedisTemplate redisTemplate;

    @Autowired
    StockOrderMapper stockOrderMapper;

    @Autowired
    StockService stockService;

    @Autowired
    RedisLock redisLock;

    @Autowired
    UserMapper userMapper;

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

//    如果遇见回滚就返回一个 Exception， 事务的传播支持当前事务，如果当前没有事务， 就新建一个事务
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public StockOrder createOrderByPessimistic(Integer sId, Integer size) {
        Stock stock = stockService.checkStockForUpdate(sId);
        int cnt = stock.getCount() - stock.getSale();
        if(cnt < size){
            throw new RuntimeException("库存不足");
        }
        stock.setSale(stock.getSale()+size);
        //刚更新库存
        stockService.saleStock(stock);
//        stockService.saleStockOptimistic(stock);
        // 创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(sId);
        stockOrder.setName(stock.getName());
        stockOrderMapper.insert(stockOrder);

        return stockOrder;
    }

    @Override
    public int createVerifiedOrder(Integer sId, Integer userId, String verifyHash) throws Exception {
        // 验证是否在抢购时间内
        log.info("请自行验证是否在抢购时间内,假设此处验证成功");

        // 验证hash值合法性
        String hashKey = CacheKey.HASH_KEY.getKey() + "_" + sId + "_" + userId;
        String verifyHashInRedis = redisTemplate.opsForValue().get(hashKey);
        if (!verifyHash.equals(verifyHashInRedis)) {
            throw new Exception("hash值与Redis中不符合");
        }
        log.info("验证hash值合法性成功");

        // 检查用户合法性
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new Exception("用户不存在");
        }
        log.info("用户信息验证成功：[{}]", user.toString());

        // 检查商品合法性
        Stock stock = stockService.checkStock(sId);
        if (stock == null) {
            throw new Exception("商品不存在");
        }
        log.info("商品信息验证成功：[{}]", stock.toString());

        //乐观锁更新库存
        stock.setSale(stock.getSale()+1);
        stockService.saleStockOptimistic(stock);
        log.info("乐观锁更新库存成功");

        //创建订单
        // 创建订单
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(sId);
        stockOrder.setName(stock.getName());
        stockOrderMapper.insert(stockOrder);

        log.info("创建订单成功");

        return stock.getCount() - (stock.getSale()+1);
    }


}
