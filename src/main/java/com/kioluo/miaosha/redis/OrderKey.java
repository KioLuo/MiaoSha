package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/7/31.
 */
public class OrderKey extends KeyPrefix {

    private static final int timeExpired = 3600 * 24 * 2;

    public OrderKey(String prefix) {
        super(prefix);
    }

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey(timeExpired, "MiaoshaOrderByUidGid");

}
