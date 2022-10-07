package com.cargodelivery.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariCP {

    private static final Logger logger = LogManager.getLogger();

    private static final HikariDataSource dataSource;

    private HikariCP() {}

    static {
        var properties = new Properties();
        try {
            var propertyFileInpStr = HikariCP.class
                    .getClassLoader()
                    .getResourceAsStream("datasource.properties");
            properties.load(propertyFileInpStr);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Failed to read database properties from property file", e);
        }
        var config = new HikariConfig(properties);
        dataSource = new HikariDataSource(config);
        logger.log(Level.INFO, "Successfully read property file and initialize datasource");
    }

    public static Connection getHikariConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
