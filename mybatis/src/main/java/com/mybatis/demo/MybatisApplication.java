package com.mybatis.demo;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  SpringBoot 整合 Mybatis
 *  使用 Mybatis Generator 根据数据库 自动生成 Dao、 Model 和 Mapper 文件
 *  需要添加 @MapperScan 注解 扫描需要的 model 包
 *
 *      秒杀测试：
 *         1、 使用 乐观锁版本控制，实现商品秒杀 数据同步
 *         2、 使用 Redis 进行并发控制
 */
@MapperScan("com.mybatis.demo.mapper")
@SpringBootApplication
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

}
