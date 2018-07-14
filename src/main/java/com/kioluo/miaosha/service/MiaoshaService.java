package com.kioluo.miaosha.service;

import com.kioluo.miaosha.domain.MiaoshaGoods;
import com.kioluo.miaosha.domain.MiaoshaOrder;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.domain.OrderInfo;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by qy_lu on 2018/6/2.
 */
@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Transactional
    public OrderInfo doMiaosha(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {
        // 减库存
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        goodsService.reduceStock(miaoshaGoods);

        // 创建订单
        OrderInfo orderInfo = orderService.createOrder(miaoshaUser, goodsVo);
        return orderInfo;
    }
}
