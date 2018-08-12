package com.kioluo.miaosha.controller;

import com.kioluo.miaosha.rabbitmq.MQSender;
import com.kioluo.miaosha.redis.RedisService;
import com.kioluo.miaosha.redis.UserKey;
import com.kioluo.miaosha.result.Result;
import com.kioluo.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by qy_lu on 2018/3/15.
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @Autowired
    MQSender mqSender;

    @RequestMapping("/thymeleaf")
    public String hello(Model model) {
        model.addAttribute("name", "xiaoli");
        return "hello";
    }

    @RequestMapping("/redis/get/{key}")
    @ResponseBody
    public Result<String> redisGet(@PathVariable("key") String key) {
        String value = redisService.get(UserKey.getById, key, String.class);
        return Result.success(value);
    }

    @RequestMapping("/redis/set/{key}/{value}")
    @ResponseBody
    public Result<String> redisSet(@PathVariable("key") String key, @PathVariable("value") String value) {
        redisService.set(UserKey.getByName, key, value);
        return Result.success(value);
    }

    @RequestMapping("/rabbitmq/send/{message}")
    @ResponseBody
    public void sendMessage(@PathVariable("message") String message) {
        mqSender.send(message);
    }

    @RequestMapping("/rabbitmq/sendTopic/{message}")
    @ResponseBody
    public void sendTopicMessage(@PathVariable("message") String message) {
        mqSender.sendTopic(message);
    }

    @RequestMapping("/rabbitmq/sendFanout/{message}")
    @ResponseBody
    public void sendFanoutMessage(@PathVariable("message") String message) {
        mqSender.sendFanout(message);
    }

    @RequestMapping("/rabbitmq/sendHeader/{message}")
    @ResponseBody
    public void sendHeaderMessage(@PathVariable("message") String message) {
        mqSender.sendHeader(message);
    }

}
