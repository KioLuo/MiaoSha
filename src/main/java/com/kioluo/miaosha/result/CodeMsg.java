package com.kioluo.miaosha.result;

/**
 * Created by qy_lu on 2018/3/31.
 */
public class CodeMsg {
    public final int code;
    public final String msg;

    // 通用错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务器端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");

    // 登录模块5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号码不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号码不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");

    // 秒杀模块5003XX
    public static CodeMsg NOSTOCK_ERROR = new CodeMsg(500310, "商品库存不足");
    public static CodeMsg REPEATMIAOSHA_ERROR = new CodeMsg(500311, "重复秒杀");



    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        String msg = String.format(this.msg, args);
        return new CodeMsg(this.code, msg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
