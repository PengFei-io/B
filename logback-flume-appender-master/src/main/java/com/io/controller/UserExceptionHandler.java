package com.io.controller;

import com.io.entities.ErrorMessage;
import com.io.exceptions.UserNameAndPasswordException;
import com.io.exceptions.VerifyCodeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
@RestControllerAdvice
public class UserExceptionHandler  {
    //@ResponseBody
    @ExceptionHandler(value = VerifyCodeException.class)
    public ErrorMessage handlerError(VerifyCodeException ex){
        return new ErrorMessage(1002,ex.getMessage());
    }

   // @ResponseBody
    @ExceptionHandler(value = UserNameAndPasswordException.class)
    public ErrorMessage handlerError(UserNameAndPasswordException ex){
        return new ErrorMessage(1001,ex.getMessage());
    }

}
