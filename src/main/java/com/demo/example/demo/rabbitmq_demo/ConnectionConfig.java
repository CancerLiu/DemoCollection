package com.demo.example.demo.rabbitmq_demo;

public class ConnectionConfig {

    private String host;
    private int port;
    private String userName;
    private String password;

    public String getHost() {
        return host;
    }

    public ConnectionConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ConnectionConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public ConnectionConfig setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ConnectionConfig setPassword(String password) {
        this.password = password;
        return this;
    }
}
