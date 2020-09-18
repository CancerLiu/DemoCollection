package com.demo.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 该项目主要作用是写各个工具的demo，如zookeeper、rocketmq、rabbitmq、kafka、elasticSearch、mock等等。同时对之做一些简单
 * 的封装。
 * 以后其他项目中需要使用时，可以做一个入门级别的参考借鉴和使用
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
