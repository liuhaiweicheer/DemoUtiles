package com.mybatis.demo.service;

import com.mybatis.demo.entity.Stock;

/**
 * @author lhw
 * @date 2020/10/5
 */
public interface StockService {
     Stock checkStock(int stockId);

     Stock checkStockForUpdate(int stockId);

     void saleStock(Stock stock);

     void saleStockOptimistic(Stock stock);

     Stock selectStockByIdForUpdate(Integer id);

    /**
     *  查询剩余库存 by 数据库
     * @param id
     * @return
     */
     int getStockCountByDB(int id);

    /**
     *  查询剩余库存 By 缓存 （Redis）
     * @param id
     * @return
     */
     Integer getStockCountByCache(int id);

    /**
     *  设置库存到缓存中去
     * @param id
     * @param count
     */
    void setStockCountCache(int id, int count);

    Integer getStockCount(int id);

    void delStockCountCache(int id);

}
