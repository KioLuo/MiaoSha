package com.kioluo.miaosha.controller;

import com.kioluo.miaosha.access.AccessLimit;
import com.kioluo.miaosha.domain.MiaoshaOrder;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.rabbitmq.MQSender;
import com.kioluo.miaosha.rabbitmq.MiaoshaMessage;
import com.kioluo.miaosha.redis.GoodsKey;
import com.kioluo.miaosha.redis.MiaoshaKey;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.result.CodeMsg;
import com.kioluo.miaosha.result.Result;
import com.kioluo.miaosha.service.GoodsService;
import com.kioluo.miaosha.service.MiaoshaService;
import com.kioluo.miaosha.service.MiaoshaUserService;
import com.kioluo.miaosha.service.OrderService;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qy_lu on 2018/4/25.
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
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

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> isOver = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods : goodsVoList) {
            redisService.set(GoodsKey.getGoodsMiaoshaStock, "" + goods.getId(), goods.getStockCount());
            isOver.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result doMiaosha(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path) {
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", miaoshaUser);

        // 验证path
        boolean pass = miaoshaService.checkMiaoshaPath(miaoshaUser.getId(), goodsId, path);
        if (!pass) {
            return Result.error(CodeMsg.REQUEST_ILLEGLE);
        }

        // 在内存中检查商品是否被秒杀完
        boolean over = isOver.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.NOSTOCK_ERROR);
        }

        // 预减库存
        long stock = redisService.decr(GoodsKey.getGoodsMiaoshaStock, "" + goodsId);
        if (stock < 0) {
            isOver.put(goodsId, true);
            return Result.error(CodeMsg.NOSTOCK_ERROR);
        }

        // 判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoshaUser.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATMIAOSHA_ERROR);
        }

        // 入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setGoodsId(goodsId);
        message.setUser(miaoshaUser);
        mqSender.sendMessage(message);
        return Result.success(0);
        /*
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
        */
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result getResult(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId) {
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", miaoshaUser);

        long result = miaoshaService.getMiaoshaResult(miaoshaUser.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    public Result getPath(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId, @RequestParam(value="verifyCode") String verifyCode) {
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", miaoshaUser);

        boolean check = miaoshaService.checkVerifyCode(miaoshaUser, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGLE);
        }

        String path = miaoshaService.createMiaoshaPATH(miaoshaUser.getId(), goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result getVerifyCode(Model model, MiaoshaUser miaoshaUser, @RequestParam("goodsId") long goodsId, HttpServletResponse response) {
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", miaoshaUser);

        BufferedImage image = miaoshaService.createVerifyCode(miaoshaUser, goodsId);
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
