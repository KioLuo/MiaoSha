package com.kioluo.miaosha.controller;

import com.kioluo.miaosha.domain.MiaoshaOrder;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.domain.OrderInfo;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.result.CodeMsg;
import com.kioluo.miaosha.service.GoodsService;
import com.kioluo.miaosha.service.MiaoshaService;
import com.kioluo.miaosha.service.MiaoshaUserService;
import com.kioluo.miaosha.service.OrderService;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by qy_lu on 2018/4/25.
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId) {
        if (miaoshaUser == null) {
            return "login";
        }
        model.addAttribute("user", miaoshaUser);

        // 库存不足
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.NOSTOCK_ERROR.getMsg());
            return "miaosha_fail";
        }

        // 重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATMIAOSHA_ERROR.getMsg());
            return "miaosha_fail";
        }

        // 减库存，下订单
        OrderInfo orderInfo = miaoshaService.doMiaosha(miaoshaUser, goodsVo);
        model.addAttribute("order", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
}
