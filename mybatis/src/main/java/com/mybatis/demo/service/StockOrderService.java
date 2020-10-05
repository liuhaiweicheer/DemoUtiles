package com.mybatis.demo.service;

import com.mybatis.demo.entity.StockOrder;

/**
 * @author lhw
 * @date 2020/10/5
 */
public interface StockOrderService {

    public StockOrder createOrder(int sId, int size);

    public StockOrder createOrderByRedisLock(int sId, int size);

}