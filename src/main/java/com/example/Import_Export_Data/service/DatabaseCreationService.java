package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class DatabaseCreationService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseCreationService.class);

    public boolean createDatabase(DestinationDbConfig config) throws SQLException {
        String systemDb = "postgres"; // default base DB to connect for admin tasks
        String systemUrl = "jdbc:postgresql://localhost:5432/" + systemDb;

        logger.info("Connecting to system database to check/create destination database: {}", config.getDbName());

        // Step 1: Connect to system DB using user-entered credentials
        try (Connection conn = DriverManager.getConnection(systemUrl, config.getUsername(), config.getPassword())) {
            logger.debug("Connected to system database successfully");
            
            ResultSet rs = conn.getMetaData().getCatalogs();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(config.getDbName())) {
                    logger.info("Database already exists: {}", config.getDbName());
                    return false; // Already exists, no need to create
                }
            }

            // Step 2: Create the new DB
            String createDbQuery = "CREATE DATABASE \"" + config.getDbName() + "\"";
            conn.createStatement().executeUpdate(createDbQuery);
            logger.info("Database created successfully: {}", config.getDbName());
        } catch (SQLException e) {
            logger.error("Failed to create database or connect to system database: {}", e.getMessage(), e);
            throw e;
        }

        // Step 3: Connect to the newly created DB to create schema
        String newDbUrl = "jdbc:postgresql://localhost:5432/" + config.getDbName();
        try (Connection dbConn = DriverManager.getConnection(newDbUrl, config.getUsername(), config.getPassword())) {
            logger.debug("Connected to new database successfully");
            
            dbConn.createStatement().executeUpdate("CREATE SCHEMA IF NOT EXISTS master");
            logger.info("Schema 'master' created or already exists");
            
            String grantSql = "GRANT ALL PRIVILEGES ON SCHEMA master TO " + config.getUsername();
            dbConn.createStatement().executeUpdate(grantSql);
            logger.info("Granted privileges to user: {}", config.getUsername());
        } catch (SQLException e) {
            logger.error("Could not create schema in new database: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Database creation process completed successfully");
        return true; // Indicates the DB was created
    }
}
