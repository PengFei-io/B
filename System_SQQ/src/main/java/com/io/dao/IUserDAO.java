package com.io.dao;

import com.io.entities.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDAO {
    void saveUser(User user);

    User queryUserByNameAndPassword(User user);

    User queryUserById(Integer id);

    void deleteByUserId(Integer id);

    List<User> queryUserByPage(
            @Param(value = "pageNow") Integer pageNow,
            @Param(value = "pageSize") Integer pageSize,
            @Param(value = "column") String column,
            @Param(value = "value") Object value);

    int queryCount(
            @Param(value = "column") String column,
            @Param(value = "value") Object value);

    void updateUser(User user);
}
