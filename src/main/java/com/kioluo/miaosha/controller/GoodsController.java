package com.kioluo.miaosha.controller;

import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.domain.OrderInfo;
import com.kioluo.miaosha.redis.GoodsKey;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.service.GoodsService;
import com.kioluo.miaosha.service.MiaoshaUserService;
import com.kioluo.miaosha.service.OrderService;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by qy_lu on 2018/4/1.
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     *
     * @param model
     * @param miaoshaUser
     * @return
     */
    @RequestMapping(value = "/to_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(Model model, MiaoshaUser miaoshaUser, HttpServletRequest request, HttpServletResponse response) {
        if (miaoshaUser == null) {
            return "login";
        }
        model.addAttribute("user", miaoshaUser);
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (StringUtils.isEmpty(html)) {
            List<GoodsVo> goodsList = goodsService.listGoodsVo();
            model.addAttribute("goodsList", goodsList);
            WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
            html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping("to_detail/{goodsId}")
    public String toDetail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId") long goodsId) {
        if (miaoshaUser == null) {
            return "login";
        }
        model.addAttribute("user",
                miaoshaUser);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (startAt > now) {
            miaoshaStatus = 0;
            remainSeconds = (int) (startAt - now) / 1000;
        } else if (now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

    @RequestMapping("to_order")
    public String toOrder(Model model, MiaoshaUser miaoshaUser, @RequestParam long orderId) {
        if (miaoshaUser == null) {
            return "login";
        }
        model.addAttribute("user",
                miaoshaUser);
        OrderInfo orderInfo = orderService.getOrderInfo(orderId);
        model.addAttribute("order", orderInfo);
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }

}
