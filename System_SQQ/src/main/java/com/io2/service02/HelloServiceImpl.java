package com.io2.service02;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {

        return "hello" + name;
    }
}
