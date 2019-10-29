package com.baizhi.controller;

import com.baizhi.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/validate")
public class UserNameValidationController {

    private static List<String> names =Arrays.asList("zhangsan","李四");

    @PostMapping(value = "/username")
    public Boolean registerUser(String name) throws IOException {
        System.out.println(name);
        return  !names.contains(name);
    }
}