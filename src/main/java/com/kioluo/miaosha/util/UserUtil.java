package com.kioluo.miaosha.util;

import com.alibaba.fastjson.JSONObject;
import com.kioluo.miaosha.domain.MiaoshaUser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qy_lu on 2018/7/14.
 */
public class UserUtil {

    public static void createAccounts() {
        createAccounts(1000L);
    }

    public static void createAccounts(long num) {
        // 生成用户账号
        List<MiaoshaUser> users = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            MiaoshaUser user = new MiaoshaUser();
            user.setId(16000000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456", "1a2b3c4d"));
            user.setRegisterDate(new Date());
            user.setLoginCount(0);
            users.add(user);
        }

        // 插入数据库
//        try {
//            Connection conn = DBUtil.getConn();
//            String sql = "insert into miaosha_user (id, nickname, password, salt, register_date, login_count) values (?, ?, ?, ?, ?, ?)";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            for (MiaoshaUser user : users) {
//                ps.setLong(1, user.getId());
//                ps.setString(2, user.getNickname());
//                ps.setString(3, user.getPassword());
//                ps.setString(4, user.getSalt());
//                ps.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
//                ps.setInt(6, user.getLoginCount());
//                ps.addBatch();
//            }
//            ps.executeBatch();
//            ps.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("已将" + num + "个账号插入数据库");

        //登录，生成token
        try {
            File file = new File("c:\\Data\\accounts.txt");
            if (file.exists()) {
                file.delete();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            file.createNewFile();
            raf.seek(0);
            String urlString = "http://localhost:8080/login/do_login";
            for (MiaoshaUser user : users) {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                out.write(("mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456")).getBytes());
                out.flush();
                String cookie = conn.getHeaderField("Set-Cookie");
                String token = cookie.substring(cookie.indexOf("=") + 1, cookie.indexOf(";"));
                System.out.println("create token: " + user.getId());
                String row = user.getId() + "," + token;
                raf.seek(raf.length());
                raf.write(row.getBytes());
                raf.write("\r\n".getBytes());
                System.out.println("write to file: " + user.getId());
            }
            raf.close();
            System.out.println("Create accounts over");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        createAccounts();
    }

}
