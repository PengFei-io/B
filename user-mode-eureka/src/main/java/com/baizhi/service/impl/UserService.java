package com.baizhi.service.impl;

import com.baizhi.dao.IUserDAO;
import com.baizhi.entities.User;
import com.baizhi.exceptions.UserNameAndPasswordException;
import com.baizhi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class UserService implements IUserService {
    @Autowired

    private IUserDAO userDAO;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    public User queryUserByNameAndPassword(User user) {
        User u = userDAO.queryUserByNameAndPassword(user);
        if (u == null) {
            throw new UserNameAndPasswordException("用户名或者密码不正确！");
        }
        return u;
    }

    public List<User> queryUserByPage(Integer pageNow, Integer pageSize, String column, Object value) {
        return userDAO.queryUserByPage(pageNow, pageSize, column, value);
    }

    @Override
    public int queryUserCount(String column, Object value) {
        return userDAO.queryCount(column, value);
    }


    public User queryUserById(Integer id) {
        return userDAO.queryUserById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void deleteByUserIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            userDAO.deleteByUserId(ids[i]);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
}
