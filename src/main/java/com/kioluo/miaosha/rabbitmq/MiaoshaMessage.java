package com.kioluo.miaosha.rabbitmq;

import com.kioluo.miaosha.domain.MiaoshaGoods;
import com.kioluo.miaosha.domain.MiaoshaUser;

/**
 * Created by qy_lu on 2018/7/31.
 */
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
