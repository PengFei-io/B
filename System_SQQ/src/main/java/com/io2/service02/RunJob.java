package com.io2.service02;

public class RunJob {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RPCServer rpcServer = new RPCServer();
        rpcServer.register(helloService, 50001);
    }
}
