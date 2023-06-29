package com.example.caloriecalculator;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface MyTestContainer {

    @Container
    @ServiceConnection
    MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:8.0.30").withInitScript("schema.sql");

}
