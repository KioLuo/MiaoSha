package com.kioluo.miaosha.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by qy_lu on 2018/7/14.
 */
@Component
public class DBUtil {
    @Autowired
    private static DruidDataSource dataSource;
    @Value("spring.datasource.url")
    private static String url = "jdbc:mysql://localhost:3306/miaosha?useUnicode=true&characterEncoding=utf8";
    @Value("spring.datasource.username")
    private static String username = "root";
    @Value("spring.datasource.password")
    private static String password = "123456";
    @Value("spring.datasource.driverClassName")
    private static String driverClassName = "com.mysql.jdbc.Driver";

    public static Connection getConn() throws SQLException {
        if (dataSource == null) {
            dataSource = new DruidDataSource();
            dataSource.setUrl(url);
            dataSource.setPassword(password);
            dataSource.setUsername(username);
            dataSource.setDriverClassName(driverClassName);
        }
        return dataSource.getConnection();
    }
}
