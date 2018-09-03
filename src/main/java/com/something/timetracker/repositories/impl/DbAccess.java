package com.something.timetracker.repositories.impl;

import org.h2.jdbcx.JdbcDataSource;
import org.jetbrains.annotations.Contract;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

public final class DbAccess {
    private static final String DEFAULT_URL = "jdbc:h2:~/.timetracker/db";
    private static NamedParameterJdbcOperations operations;
    private static JdbcDataSource dataSource;

    public static void configureDefault() {
        configure(DEFAULT_URL);
    }

    static void configure(String dbUrl) {
        dataSource = new JdbcDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUser("");
        dataSource.setPassword("");
        operations = new NamedParameterJdbcTemplate(dataSource);
    }

    @Contract(pure = true)
    public static NamedParameterJdbcOperations getDbOperations() {
        return operations;
    }

    static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
