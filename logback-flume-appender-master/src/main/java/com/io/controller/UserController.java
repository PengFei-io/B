package com.io.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.io.entities.User;
import com.io.exceptions.UserNameAndPasswordException;
import com.io.exceptions.VerifyCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Controller
@RequestMapping(value = "/userApp")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/registerUser")
    @ResponseBody
    public String registerUser(User user,
                               @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile,
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


        } else {
            throw new VerifyCodeException("验证码错误！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(user.getBirthDay());
        System.out.println("日期：" + user.getBirthDay());
        String url = "http://localhost:8888/root/formUserManager_v1/registerUser";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("name", user.getName());
        params.add("password", user.getPassword());
        params.add("birthDay", date);
        params.add("sex", String.valueOf(user.getSex()));
        params.add("photo", user.getPhoto());
        params.add("email", user.getEmail());
        User user1 = restTemplate.postForObject(url, params, User.class);
        return user.getEmail();

    }

    @PostMapping(value = "/addUser")
    @ResponseBody
    public void addUser(User user,
                        HttpSession session,
                        @RequestParam(value = "multipartFile", required = false)
                                MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            LOGGER.debug("存储文件:" + multipartFile.getOriginalFilename());
            InputStream inputStream = multipartFile.getInputStream();
            FastImageFile png = new FastImageFile(inputStream, inputStream.available(),
                    "png", new HashSet<>());
            StorePath storePath = fastFileStorageClient.uploadImage(png);
            LOGGER.debug("存储文件路径:", storePath);
            user.setPhoto(multipartFile.getOriginalFilename());
        }
        //调用远程注册服务实现注册
        String url = "http://localhost:8888/root/manageUser_v2/addUser";
        session.setAttribute("user", user);
        restTemplate.postForObject(url, user, User.class);
    }

    @GetMapping(value = "/queryUserById")
    @ResponseBody
    public User queryUserById(@RequestParam(value = "id") Integer id) {
        //调用远程注册服务实现注册
        String url = "http://localhost:8888/root/manageUser_v2/queryUserById?id={id}";
        Map<String, String> params = new HashMap<String, String>();
        User user = restTemplate.getForObject(url, User.class, params);
        return user;
    }

    @GetMapping(value = "/queryUserByPage")
    @ResponseBody
    public Map<String, Object> queryUserByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNow,
                                               @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "column", required = false, defaultValue = "name") String column,
                                               @RequestParam(value = "value", required = false) String value) {

        String url = "http://localhost:8888/root/manageUser_v2/queryUserByPage?page={page}&rows={rows}&column={column}&value={value}";

        Map<String, String> params = new HashMap();
        params.put("page", String.valueOf(pageNow));
        params.put("rows", String.valueOf(pageSize));
        params.put("column", column);
        params.put("value", value);


        User[] users = restTemplate.getForObject(url, User[].class, params);

        Integer counts = 0;
        if (users != null && users.length > 0) {
            url = "http://localhost:8888/root/manageUser_v2/queryUserCount?column={column}&value={value}";
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
        String url = "http://localhost:8888/root/manageUser_v2/delteUserByIds?ids={ids}";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", Arrays.asList(ids).stream().map(item -> item.toString()).reduce((v1, v2) -> v1 + "," + v2).get());
        restTemplate.delete(url, params);
    }

    @PutMapping(value = "/updateUser")
    @ResponseBody
    public void updateUser(User user, @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {

        String url = "http://localhost:8888/root/manageUser_v2/updateUser";
        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            LOGGER.debug("更新新的路径，删除远程文件系统");
            user.setPhoto(multipartFile.getOriginalFilename());
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
        LOGGER.debug("userLogin:" + user);
        LOGGER.debug("verifyCode:" + verifyCode);
        LOGGER.debug("inputVector:" + inputVector);
        LOGGER.debug("agentInfo:" + agentInfo);

        String sessionVerifyCode = (String) session.getAttribute("verifyCode");
        if (sessionVerifyCode != null && verifyCode != null && verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
            //调用远程登录服务实现注册
            String url = "http://localhost:8888/root/manageUser_v2/userLogin";
            User loginUser = restTemplate.postForObject(url, user, User.class);
            if (loginUser == null) {
                throw new UserNameAndPasswordException("用户名，密码不存在!");
            }
            session.setAttribute("user", loginUser);
        } else {
            throw new VerifyCodeException("验证码错误！");
        }
    }

    @GetMapping(value = "/sessionUser")
    @ResponseBody
    public User sessionUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user;
    }
}