package com.kioluo.miaosha.service;

import com.kioluo.miaosha.dao.MiaoshaUserDao;
import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.exception.GlobalException;
import com.kioluo.miaosha.redis.MiaoshaUserKey;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.result.CodeMsg;
import com.kioluo.miaosha.util.MD5Util;
import com.kioluo.miaosha.util.UUIDUtil;
import com.kioluo.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qy_lu on 2018/4/1.
 */
@Service
public class MiaoshaUserService {

    @Autowired
    private RedisService redisService;

    public static final String COOKIE_TOKEN_NAME = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

        // 重新设置session
        if (miaoshaUser != null) {
            addCookie(response, token, miaoshaUser);
        }
        return miaoshaUser;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        // 判断手机号是否存在
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 验证密码
        String dbPass = miaoshaUser.getPassword();
        String calPass = MD5Util.formPassToDBPass(password, miaoshaUser.getSalt());
        if (!dbPass.equals(calPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // 生成token
        String token = UUIDUtil.uuid();
        addCookie(response, token, miaoshaUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {
        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_TOKEN_NAME, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
