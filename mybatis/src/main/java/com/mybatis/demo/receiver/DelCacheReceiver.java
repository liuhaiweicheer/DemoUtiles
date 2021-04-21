package com.mybatis.demo.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lhw
 * @date 2020/10/10
 */
@Component
@RabbitListener(queues = "delCache")
@Slf4j
public class DelCacheReceiver {

    @RabbitHandler
    public void process(String msg){
        log.info("【接收到消息】 处理 delCache 消息队列中的请求: " + msg);
    }

}
