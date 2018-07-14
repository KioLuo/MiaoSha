package com.kioluo.miaosha.redis;

/**
 * Created by qy_lu on 2018/3/17.
 */
public class UserKey extends KeyPrefix {

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

    public UserKey(String prefix) {
        super(prefix);
    }
}
