package com.kioluo.miaosha.rabbitmq;

import com.kioluo.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qy_lu on 2018/7/26.
 */
@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
        log.info("send message: " + msg);
    }

    public void sendTopic(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg);
        log.info("send topic message: " + msg);
    }

    public void sendFanout(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
        log.info("send fanout message: " + msg);
    }

    public void sendHeader(Object message) {
        String msg = RedisService.beanToString(message);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE, "", obj);
        log.info("send header message: " + msg);
    }

    public void sendMessage(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
        log.info("send message: " + msg);
    }
}
