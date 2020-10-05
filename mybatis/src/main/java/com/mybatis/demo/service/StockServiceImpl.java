package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lhw
 * @date 2020/10/5
 */
@Service
public class StockServiceImpl implements StockService{

    @Autowired
    StockMapper stockMapper;

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


}
