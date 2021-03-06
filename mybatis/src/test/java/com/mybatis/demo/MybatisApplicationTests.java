package com.mybatis.demo;

import com.mybatis.demo.entity.Stock;
import com.mybatis.demo.entity.User;
import com.mybatis.demo.mapper.StockMapper;
import com.mybatis.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    StockMapper stockMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testMybatis(){
        User user = new User();
        user.setUserName("小红花");
        user.setPassword("123456");
        user.setPhone("1321131546");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test
    public void testStock(){
        Stock stock = stockMapper.selectByPrimaryKeyForUpdate(1);
        System.out.println(stock);
    }

}
