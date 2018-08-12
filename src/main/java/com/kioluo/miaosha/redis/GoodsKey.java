package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/4/1.
 */
public class GoodsKey extends KeyPrefix {

    private static final int timeExpired = 3600 * 24 * 2;

    public GoodsKey(int timeExpired, String prefix) {
        super (timeExpired, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(timeExpired, "GoodsList");
    public static GoodsKey getGoodsDetail = new GoodsKey(timeExpired, "GoodsDetail");
    public static GoodsKey getGoodsMiaoshaStock = new GoodsKey(timeExpired, "GoodsMiaoshaStock");

}
