package com.demo.example.demo.rabbitmq_demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQFactory {

    private final static Logger logger = LoggerFactory.getLogger(RabbitMQFactory.class);

    public static Channel construct(ConnectionConfig config) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());
        factory.setUsername(config.getUserName());
        factory.setPassword(config.getPassword());

        Connection connection = factory.newConnection();
        return connection.createChannel();
    }


}
