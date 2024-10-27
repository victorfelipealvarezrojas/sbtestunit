package com.testing.sbtest.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;


/** This is a singleton */
public abstract class AbstractContainerBaseTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa");

        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public  static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",       MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password",  MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username",  MY_SQL_CONTAINER::getUsername);
    }
}
