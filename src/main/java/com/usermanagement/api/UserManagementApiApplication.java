package com.usermanagement.api;

import com.usermanagement.api.entity.User;
import com.usermanagement.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/**
 * Main Spring Boot Application Entry Point for User Management REST API.
 * Module 5: Java Web & REST API Development
 */
@SpringBootApplication
public class UserManagementApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementApiApplication.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(UserManagementApiApplication.class, args);
        String port = context.getEnvironment().getProperty("server.port", "8081");
        logger.info("==================================================================");
        logger.info("  User Management REST API Application is RUNNING on port {}!     ", port);
        logger.info("  API Base URL: http://localhost:{}/api/users                     ", port);
        logger.info("  H2 Console:   http://localhost:{}/h2-console                    ", port);
        logger.info("==================================================================");
    }

    /**
     * Seeds initial demonstration data on application startup.
     *
     * @param userRepository repository for persisting user records
     * @return CommandLineRunner to execute data initialization
     */
    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user1 = User.builder()
                        .name("Alice Johnson")
                        .email("alice.johnson@example.com")
                        .age(28)
                        .createdAt(LocalDateTime.now())
                        .build();

                User user2 = User.builder()
                        .name("Bob Smith")
                        .email("bob.smith@example.com")
                        .age(34)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(user1);
                userRepository.save(user2);
                logger.info("Seeded initial demo users: Alice Johnson & Bob Smith");
            }
        };
    }
}
