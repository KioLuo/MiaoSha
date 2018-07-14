package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/4/1.
 */
public class MiaoshaUserKey extends KeyPrefix {

    private static final int timeExpired = 3600 * 24 * 2;

    public MiaoshaUserKey(int timeExpired, String prefix) {
        super (timeExpired, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(timeExpired, "tk");
}
