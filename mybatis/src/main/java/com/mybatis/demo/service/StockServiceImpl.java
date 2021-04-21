package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.mapper.StockMapper;
import com.mybatis.demo.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lhw
 * @date 2020/10/5
 */
@Slf4j
@Service
public class StockServiceImpl implements StockService{

    @Autowired
    StockMapper stockMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     *  检查库存获取 存货量
     * @param stockId  库存Id
     * @return 库存信息
     */
    public Stock checkStock(int stockId){
        Stock stock = stockMapper.selectByPrimaryKey(stockId);
        if(stock.getCount().equals(stock.getSale())){
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    @Override
    public Stock checkStockForUpdate(int stockId) {
        Stock stock = selectStockByIdForUpdate(stockId);
        if(stock.getCount().equals(stock.getSale())){
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    public void saleStock(Stock stock){
        stockMapper.updateByPrimaryKey(stock);
    }

    @Override
    public void saleStockOptimistic(Stock stock) {
        int cnt = stockMapper.updateOptimisticByPrimaryKey(stock);
        if(cnt == 0){
            throw new RuntimeException("并发更新数据库失败，秒杀失败再试试~~");
        }
    }

    @Override
    public Stock selectStockByIdForUpdate(Integer id) {
        return stockMapper.selectByPrimaryKeyForUpdate(id);
    }

    @Override
    public int getStockCountByDB(int id) {
        Stock stock = stockMapper.selectByPrimaryKey(id);
        return stock.getCount() - stock.getSale();
    }

    @Override
    public Integer getStockCountByCache(int id) {
        String hashKey = CacheKey.STOCK_COUNT.getKey() + "_" + id;
        String countStr = redisTemplate.opsForValue().get(hashKey);
        if(countStr != null){
            return Integer.parseInt(countStr);
        }else {
            return null;
        }
    }

    @Override
    public void setStockCountCache(int id, int count) {
        String hashKey = CacheKey.STOCK_COUNT.getKey() + "_" + id;
        String countStr = String.valueOf(count);
        log.info("写入商品库存缓存: [{}] [{}]", hashKey, countStr);
        redisTemplate.opsForValue().set(hashKey, countStr, 3600, TimeUnit.SECONDS);
    }

    @Override
    public Integer getStockCount(int sid) {
        Integer stockLeft;
        stockLeft = getStockCountByCache(sid);
        log.info("缓存中取得库存数：[{}]", stockLeft);
        if (stockLeft == null) {
            stockLeft = getStockCountByDB(sid);
            log.info("缓存未命中，查询数据库，并写入缓存");
            setStockCountCache(sid, stockLeft);
        }
        return stockLeft;
    }

    @Override
    public void delStockCountCache(int id) {
        String keyHash = CacheKey.STOCK_COUNT.getKey() + "_" +id;
        redisTemplate.delete(keyHash);
        log.info("【删除 Redis 缓存】");
    }


}
