package com.baizhi.controller;

import com.baizhi.entities.User;
import com.baizhi.exceptions.UserNameAndPasswordException;
import com.baizhi.exceptions.VerifyCodeException;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "/formUserManager")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/registerUser")
    @ResponseBody
    public void registerUser(User user,
                             @RequestParam(value = "multipartFile", required = false)
                                     MultipartFile multipartFile,
                             @RequestParam(value = "verifyCode") String verifyCode,
                             HttpSession session) throws IOException {

        LOGGER.debug("verifyCode:" + verifyCode);
        LOGGER.debug("multipartFile:" + multipartFile.getOriginalFilename());

        //获取会话verifyCode
        String sessionVerifyCode = (String) session.getAttribute("verifyCode");

        if (sessionVerifyCode != null && verifyCode != null && verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
            //删除此次的会话verifyCode
            session.removeAttribute("verifyCode");
            if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
                LOGGER.debug("存储文件:" + multipartFile.getOriginalFilename());
                user.setPhoto(multipartFile.getOriginalFilename());
            }
            //调用远程登录服务实现注册
            String url = "http://USER-MODEL/UserModel/manageUser_v2/registerUser";
            User registerUser = restTemplate.postForObject(url, user, User.class);
            session.setAttribute("user", registerUser);
        } else {
            throw new VerifyCodeException("验证码错误！");
        }
    }

    @PostMapping(value = "/addUser")
    @ResponseBody
    public void addUser(User user,
                        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {

    }

    @GetMapping(value = "/queryUserById")
    @ResponseBody
    public User queryUserById(@RequestParam(value = "id") Integer id) {
        return null;
    }

    @GetMapping(value = "/queryUserByPage")
    @ResponseBody
    public Map<String, Object> queryUserByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNow,
                                               @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "column", required = false, defaultValue = "name") String column,
                                               @RequestParam(value = "value", required = false) String value) {

        String url = "http://USER-MODEL/UserModel/manageUser_v2/queryUserByPage?page={page}&rows={rows}&column={column}&value={value}";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", pageNow);
        params.put("rows", pageSize);
        params.put("column", column);
        params.put("value", value);


        User[] users = restTemplate.getForObject(url, User[].class, params);

        Integer counts = 0;
        if (users != null && users.length > 0) {
            url = "http://USER-MODEL/UserModel/manageUser_v2/queryUserCount?column={column}&value={value}";
            counts = restTemplate.getForObject(url, Integer.class, params);
        }

        HashMap<String, Object> results = new HashMap<>();
        results.put("total", counts);
        results.put("rows", users);
        return results;
    }


    @DeleteMapping(value = "/delteUserByIds")
    @ResponseBody
    public void delteUserByIds(@RequestParam(value = "ids") Integer[] ids) {

    }

    @PutMapping(value = "/updateUser")
    @ResponseBody
    public void updateUser(User user,
                           @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {
        System.out.println(user + " " + multipartFile.getOriginalFilename());
        String url = "http://USER-MODEL/UserModel/manageUser_v2/updateUser";
        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            //文件长传
            InputStream inputStream = multipartFile.getInputStream();
            long fileSize = inputStream.available();
            Set<MetaData> metaDatas = new HashSet<>();
            String fileName = multipartFile.getOriginalFilename();
            String[] tokens = fileName.split("\\.");
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, fileSize, tokens[tokens.length - 1], metaDatas);
            user.setPhoto(storePath.getFullPath());
        }
        restTemplate.put(url, user);

    }

    @PostMapping(value = "/userLogin")
    @ResponseBody
    public void userLogin(User user,
                          @RequestParam(value = "verifyCode") String verifyCode,
                          HttpSession session,
                          HttpServletRequest request,
                          @CookieValue(name = "inputVector", required = false) String inputVector,
                          @RequestHeader(name = "User-Agent") String agentInfo) {

        String sessionVerifyCode = (String) session.getAttribute("verifyCode");

        if (sessionVerifyCode != null && verifyCode != null && verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
            //删除此次的会话verifyCode
            session.removeAttribute("verifyCode");
            //调用远程登录服务实现注册
            String url = "http://USER-MODEL/UserModel/manageUser_v2/userLogin";
            User loginUser = null;
            try {
                loginUser = restTemplate.postForObject(url, user, User.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw new UserNameAndPasswordException("用户名字，密码不存在!");
            }
            session.setAttribute("user", loginUser);
        } else {
            throw new VerifyCodeException("验证码错误！");
        }
    }

    @GetMapping(value = "/sessionUser")
    @ResponseBody
    public User querySession(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}