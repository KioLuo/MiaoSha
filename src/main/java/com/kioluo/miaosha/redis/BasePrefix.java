package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/3/17.
 */
public interface BasePrefix {
    int expireSeconds();

    String getPrefix();
}
