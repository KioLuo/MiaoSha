package com.kioluo.miaosha.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by qy_lu on 2018/3/31.
 */
public class ValidatorUtilTest {
    @Test
    public void isMobile() throws Exception {
        String rightPhone = "18555555555";
        String wrongPhone = "122222222222";
        assertEquals(ValidatorUtil.isMobile(rightPhone), true);
        assertEquals(ValidatorUtil.isMobile(wrongPhone), false);


    }

}