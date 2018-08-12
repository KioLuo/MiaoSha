package com.kioluo.miaosha.service;

import com.kioluo.miaosha.dao.GoodsDao;
import com.kioluo.miaosha.domain.MiaoshaGoods;
import com.kioluo.miaosha.redis.GoodsKey;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qy_lu on 2018/4/3.
 */
@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private RedisService redisService;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(MiaoshaGoods miaoshaGoods) {
        int ret = goodsDao.reduceStock(miaoshaGoods);
        return ret > 0;
    }
}
