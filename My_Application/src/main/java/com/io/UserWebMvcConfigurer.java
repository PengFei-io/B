package com.io;

import com.io.interceptors.UserForceLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new UserForceLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/userApp/addUser")
                .excludePathPatterns("/userApp/userLogin")
                .excludePathPatterns("/commons/**")
                .excludePathPatterns("/kaptcha/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/statics/**")
                .excludePathPatterns("/login.html")


        ;


    }
}
