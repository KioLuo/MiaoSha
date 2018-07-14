package com.kioluo.miaosha.controller;

import com.kioluo.miaosha.domain.MiaoshaUser;
import com.kioluo.miaosha.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by qy_lu on 2018/7/7.
 */
@Controller
public class UserController {

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(MiaoshaUser user) {
        return Result.success(user);
    }
}
