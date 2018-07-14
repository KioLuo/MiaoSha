package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/3/17.
 */
public abstract class KeyPrefix implements BasePrefix {
    private int expireSeconds;
    private String prefix;

    public KeyPrefix(String prefix) {
        this(0, prefix);
    }

    public KeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
