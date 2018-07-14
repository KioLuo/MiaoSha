package com.kioluo.miaosha.util;

import java.util.UUID;

/**
 * Created by qy_lu on 2018/4/1.
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
