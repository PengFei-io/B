package com.baizhi;

import com.baizhi.interceptors.ForceUserLogin;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserMvcConfiogure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new ForceUserLogin())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/index.html",
//                        "/commons/**",
//                        "/statics/**",
//                        "/formUserManager/userLogin",
//                        "/formUserManager/registerUser",
//                        "/kaptcha/**",
//                        "/validate/**"
//                );
    }
}
