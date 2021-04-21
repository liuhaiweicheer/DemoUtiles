package com.mybatis.demo.service;

import com.mybatis.demo.entity.StockOrder;

/**
 * @author lhw
 * @date 2020/10/5
 */
public interface StockOrderService {

    StockOrder createOrder(int sId, int size);

    StockOrder createOrderByRedisLock(int sId, int size);

    StockOrder createOrderByPessimistic(Integer sId, Integer size);

    int createVerifiedOrder(Integer sid, Integer userId, String verifyHash) throws Exception;



}
