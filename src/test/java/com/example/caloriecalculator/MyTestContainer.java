package com.example.caloriecalculator;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface MyTestContainer {

    @Container
    MySQLContainer mysqlContainer = new MySQLContainer<>("mysql:8.0.30").withInitScript("schema.sql");
}
