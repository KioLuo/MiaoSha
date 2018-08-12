package com.kioluo.miaosha.access;

import com.kioluo.miaosha.domain.MiaoshaUser;

/**
 * Created by qy_lu on 2018/8/12.
 */
public class UserContext {
    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }
}
