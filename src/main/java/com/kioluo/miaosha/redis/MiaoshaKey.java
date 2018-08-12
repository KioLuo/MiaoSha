package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/8/5.
 */
public class MiaoshaKey extends KeyPrefix {
    private static final int timeExpired = 3600 * 24 * 2;

    public MiaoshaKey(String prefix) {
        super(timeExpired, prefix);
    }

    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static final MiaoshaKey getGoodsOver = new MiaoshaKey("GoodsOver");
    public static final MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "MiaoshaPath");
    public static final MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(60 * 5, "MiaoshaVerifyCode");
}
