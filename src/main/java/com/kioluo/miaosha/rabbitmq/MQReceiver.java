package com.kioluo.miaosha.rabbitmq;

import com.kioluo.miaosha.domain.MiaoshaOrder;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.domain.OrderInfo;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.service.GoodsService;
import com.kioluo.miaosha.service.MiaoshaService;
import com.kioluo.miaosha.service.OrderService;
import com.kioluo.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qy_lu on 2018/7/26.
 */
@Service
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message: " + message);
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = miaoshaMessage.getUser();
        long goodsId = miaoshaMessage.getGoodsId();
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderInfo orderInfo = miaoshaService.doMiaosha(user, goodsVo);

    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info("receive topic1: " + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info("receive topic2: " + message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE1)
    public void receiveHeader1(byte[] message) {
        log.info("receive header1: " + new String(message));
    }
}
