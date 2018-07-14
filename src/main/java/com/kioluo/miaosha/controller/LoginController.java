package com.kioluo.miaosha.controller;

import com.kioluo.miaosha.result.CodeMsg;
import com.kioluo.miaosha.result.Result;
import com.kioluo.miaosha.service.MiaoshaUserService;
import com.kioluo.miaosha.util.ValidatorUtil;
import com.kioluo.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by qy_lu on 2018/3/20.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    static private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<?> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());

        // 登录
        miaoshaUserService.login(response, loginVo);
        return Result.success(true);
    }
}
