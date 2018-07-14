package com.kioluo.miaosha.service;

import com.kioluo.miaosha.dao.UserDao;
import com.kioluo.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qy_lu on 2018/3/15.
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }
}
