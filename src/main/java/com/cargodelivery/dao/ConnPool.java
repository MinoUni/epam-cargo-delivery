package com.cargodelivery.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnPool {

    private static final Logger LOG = LoggerFactory.getLogger(ConnPool.class);

    private static DataSource dataSource;

    private ConnPool() {}

    static {
        try {
            Context context = new InitialContext();
            Context envContext = (Context) context.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/epam");
            LOG.info("Read datasource successfully, dataSource={}", dataSource);
        } catch (NamingException e) {
            LOG.error("Error", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
