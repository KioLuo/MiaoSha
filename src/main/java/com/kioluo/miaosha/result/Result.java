package com.kioluo.miaosha.result;

/**
 * Created by qy_lu on 2018/3/17.
 */
public class Result<T> {
    private int code;
    private T data;
    private String msg;

    public Result(T data) {
        this.data = data;
    }
    public Result() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static <K> Result<K> success(K data) {
        Result<K> result = new Result<>(data);
        result.setCode(0);
        return result;
    }

    public static Result error(CodeMsg codeMsg) {
        Result result = new Result<>();
        result.setCode(codeMsg.code);
        result.setMsg(codeMsg.msg);
        return result;
    }
}
