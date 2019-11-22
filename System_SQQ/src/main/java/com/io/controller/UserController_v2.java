package com.io.controller;

import com.io.entities.User;
import com.io.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //等价Controller + ResponseBody
@RequestMapping(value = "manageUser_v2")
public class UserController_v2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController_v2.class);

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/registerUser") //post insert
    public User registerUser(@RequestBody User user) {
        LOGGER.debug(user.toString());
        userService.saveUser(user);
        return user;
    }

    @PostMapping(value = "/addUser")
    public User addUser(@RequestBody User user) {
        LOGGER.debug(user.toString());
        userService.saveUser(user);
        return user;
    }

    @GetMapping(value = "/queryUserById") //get 查询
    public User queryUserById(@RequestParam(value = "id") Integer id) {
        LOGGER.debug(id + "");
        //从数据库中查询
        return userService.queryUserById(id);
    }

    @GetMapping(value = "/queryUserByPage") //分页查询
    public List<User> queryUserByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNow,
                                      @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "column", required = false) String column,
                                      @RequestParam(value = "value", required = false) String value) {
        LOGGER.debug(pageNow + " ," + pageSize + " ," + column + "," + value);

        return userService.queryUserByPage(pageNow, pageSize, column, value);
    }

    @GetMapping(value = "/queryUserCount") //查询总记录数
    public Integer queryUserCount(@RequestParam(value = "column", required = false) String column,
                                  @RequestParam(value = "value", required = false) String value) {
        LOGGER.debug(column + "," + value);
        return userService.queryUserCount(column, value);
    }


    @DeleteMapping(value = "/delteUserByIds") //删除
    public void delteUserByIds(@RequestParam(value = "ids") Integer[] ids) {
        LOGGER.debug(ids.toString());
        userService.deleteByUserIds(ids);
    }

    @PutMapping(value = "/updateUser") //更新用户
    public void updateUser(@RequestBody User user) {
        LOGGER.debug(user.toString());
        //更新用户信息
        userService.updateUser(user);
    }

    @PostMapping(value = "/userLogin") //用户登录
    public User userLogin(@RequestBody User user) {
        LOGGER.debug(user.toString());
        return userService.queryUserByNameAndPassword(user);
    }
}
