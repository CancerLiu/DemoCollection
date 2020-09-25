package com.demo.example.demo.dynamic_proxy_demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 封装一个用于实时生成传入类的动态代理的工具类
 */
public class DynamicProxyUtils {
    public static void main(String[] args) {
        Person stu = new Student();
        InvocationHandler handler = new StuInvocationHandler(stu);
        Person person = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{Person.class}, handler);
        person.outputName();
    }
}
