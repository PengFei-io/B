package com.baizhi.exceptions;

import com.baizhi.entities.ErrorMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class UserExceptionHandller  {

    @ExceptionHandler(value = VerifyCodeException.class)
    @ResponseBody
    public ErrorMessage verifyCodeException(VerifyCodeException e){
        return new ErrorMessage(1010,e.getMessage());
    }

    @ExceptionHandler(value = UserNameAndPasswordException.class)
    @ResponseBody
    public ErrorMessage userNameAndPasswordException(UserNameAndPasswordException e){
        System.err.println("用户名密码异常");
        return new ErrorMessage(1020,e.getMessage());
    }


}
