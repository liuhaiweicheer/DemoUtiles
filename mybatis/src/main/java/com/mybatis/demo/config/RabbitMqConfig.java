package com.mybatis.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author lhw
 * @date 2020/10/10
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue delCacheQueue(){
        return  new Queue("delCache");
    }

}
