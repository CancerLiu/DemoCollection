package com.demo.example.demo.rabbitmq_demo.producer;

import com.demo.example.demo.rabbitmq_demo.ConnectionConfig;
import com.demo.example.demo.rabbitmq_demo.RabbitMQFactory;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    private ConnectionConfig config;

    public Producer(ConnectionConfig config) {
        this.config = config;
    }

    public void send() throws IOException, TimeoutException {
        Channel channel = RabbitMQFactory.construct(config);

    }

}
