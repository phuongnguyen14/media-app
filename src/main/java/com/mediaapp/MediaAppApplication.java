package com.mediaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Media Application - Main Application Class
 * 
 * Spring Boot application cho Media App backend
 * Hỗ trợ PostgreSQL, Elasticsearch, và Flyway migration
 * 
 * @author Development Team
 * @version 1.0.0
 */
@SpringBootApplication
public class MediaAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaAppApplication.class, args);
    }
}
