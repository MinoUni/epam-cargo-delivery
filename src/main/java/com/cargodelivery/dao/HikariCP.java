package com.cargodelivery.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCP {

    private static final Logger LOG = LoggerFactory.getLogger(HikariCP.class);

    private static DataSource dataSource;

    private HikariCP() {}

    static {
        try {
            Context context = new InitialContext();
            Context envContext = (Context) context.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/epam");
            LOG.info("OK, dataSource={}", dataSource);
        } catch (NamingException e) {
            LOG.error("Error", e);
        }
    }

    public static Connection getHikariConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
