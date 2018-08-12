package com.kioluo.miaosha.service;

import com.kioluo.miaosha.dao.OrderDao;
import com.kioluo.miaosha.domain.MiaoshaOrder;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.domain.OrderInfo;
import com.kioluo.miaosha.redis.OrderKey;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by qy_lu on 2018/6/2.
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
        MiaoshaOrder order = orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return order;
    }

    public long insert(OrderInfo orderInfo) {
        return orderDao.insert(orderInfo);
    }

    public void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder) {
        orderDao.insertMiaoshaOrder(miaoshaOrder);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {
        // 创建orderinfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(miaoshaUser.getId());
        orderDao.insert(orderInfo);
        // 创建miaosha_order
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(miaoshaUser.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid, miaoshaUser.getId() + "_" + goodsVo.getId(), miaoshaOrder);
        return orderInfo;
    }

    public OrderInfo getOrderInfo(long orderId) {
        return orderDao.getOrderInfo(orderId);
    }
}
