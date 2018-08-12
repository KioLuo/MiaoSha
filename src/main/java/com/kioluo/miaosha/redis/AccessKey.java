package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/8/5.
 */
public class AccessKey extends KeyPrefix {
    private static final int timeExpired = 3600 * 24 * 2;

    public AccessKey(String prefix) {
        super(timeExpired, prefix);
    }

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static final AccessKey withExpired(int seconds) {
        return new AccessKey(seconds, "AK");
    }
}
