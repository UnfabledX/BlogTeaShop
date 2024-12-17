package com.leka.blogteashop;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
public abstract class BaseItTest {

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:17.2");

}
