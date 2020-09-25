package com.demo.example.demo.dynamic_proxy_demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StuInvocationHandler implements InvocationHandler {

    Person target;

    public StuInvocationHandler(Person stu) {
        this.target = stu;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(proxy.getClass().getName());
        System.out.println("哈哈");
        method.invoke(target, args);
        System.out.println("喜嘻嘻");
        return null;
    }
}
