package com.kioluo.miaosha.dao;

import com.kioluo.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by qy_lu on 2018/4/1.
 */
@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id") long id);

    @Insert("insert into miaosha_user (id, nickname, password, salt, head, register_date, last_login_date, login_count) values (#{id}, #{nickname}, #{password}, #{salt}, #{head}, #{registerDate}, #{lastLoginDate}, #{loginCount})")
    void addUser(MiaoshaUser user);
}
