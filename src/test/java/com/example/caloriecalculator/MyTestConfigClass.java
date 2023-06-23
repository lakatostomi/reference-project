package com.example.caloriecalculator;

import com.example.caloriecalculator.util.CommonDataInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
@ImportTestcontainers(MyTestContainer.class)
public class MyTestConfigClass {

    @Transactional
    @Bean(initMethod = "initData")
    public CommonDataInitializer commonDataInitializer() {
        return new CommonDataInitializer();
    }

}
