package com.kioluo.miaosha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qy_lu on 2018/3/31.
 */
public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }
}
