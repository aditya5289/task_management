package myCompany.taskManagement.userService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.datasource.dbname}")
    private String dbName;

    @PostConstruct
    public void init() {
        try (Connection connection = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword)) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
                logger.info("Database {} created or already exists.", dbName);
            }
        } catch (SQLException e) {
            logger.error("Failed to create database: {}", e.getMessage());
        }
    }
}
