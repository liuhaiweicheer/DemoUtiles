package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;

/**
 * @author lhw
 * @date 2020/10/5
 */
public interface StockService {
    public Stock checkStock(int stockId);

    public void saleStock(Stock stock);

    public void saleStockOptimistic(Stock stock);

}
