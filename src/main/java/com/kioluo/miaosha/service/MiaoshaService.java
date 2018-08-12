package com.kioluo.miaosha.service;

import com.kioluo.miaosha.domain.MiaoshaGoods;
import com.kioluo.miaosha.domain.MiaoshaOrder;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.domain.OrderInfo;
import com.kioluo.miaosha.redis.MiaoshaKey;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.util.MD5Util;
import com.kioluo.miaosha.util.UUIDUtil;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by qy_lu on 2018/6/2.
 */
@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo doMiaosha(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {
        // 减库存
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        boolean success = goodsService.reduceStock(miaoshaGoods);
        if (success) {
            // 创建订单
            OrderInfo orderInfo = orderService.createOrder(miaoshaUser, goodsVo);
            return orderInfo;
        } else {
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.getGoodsOver, "" + goodsId, true);
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.getGoodsOver, "" + goodsId);
    }

    public String createMiaoshaPATH(Long id, long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + id + "_" + goodsId, path);
        return path;
    }

    public boolean checkMiaoshaPath(Long id, Long goodsId, String path) {
        if (id == null || goodsId == null || path == null) {
            return false;
        }
        String newPath = redisService.get(MiaoshaKey.getMiaoshaPath, "" + id + "_" + goodsId, String.class);
        return path.equals(newPath);
    }

    public BufferedImage createVerifyCode(MiaoshaUser miaoshaUser, long goodsId) {
        if (miaoshaUser == null || goodsId < 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        // create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        // 把验证码放到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId() + "," + goodsId, rnd);
        return image;
    }

    private int calc(String verifyCode) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (int) engine.eval(verifyCode);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] opts = {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char opt1 = opts[rdm.nextInt(3)];
        char opt2 = opts[rdm.nextInt(3)];
        return "" + num1 + opt1 + num2 + opt2 + num3;
    }

    public boolean checkVerifyCode(MiaoshaUser miaoshaUser, long goodsId, String verifyCode) {
        Integer rightCode = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId() + "," + goodsId, Integer.class);
        if (verifyCode == null || rightCode == null || "".equals(verifyCode) || Integer.parseInt(verifyCode) - rightCode != 0) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, miaoshaUser.getId() + "," + goodsId);
        return true;
    }
}
