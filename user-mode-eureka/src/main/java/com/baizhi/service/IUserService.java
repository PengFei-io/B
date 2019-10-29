package com.baizhi.service;


import com.baizhi.entities.User;

import java.util.List;

public interface IUserService {
    void saveUser(User user);
    User queryUserByNameAndPassword(User user);
    /***
     *
     * @param pageNow
     * @param pageSize
     * @param column 模糊查询列
     * @param value  模糊值
     * @return
     */
    List<User> queryUserByPage(Integer pageNow, Integer pageSize,
                               String column, Object value);
    int queryUserCount(String column, Object value);
    User queryUserById(Integer id);
    void deleteByUserIds(Integer[] ids);
    void updateUser(User user);
}
