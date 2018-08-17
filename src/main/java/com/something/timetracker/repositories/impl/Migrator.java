package com.something.timetracker.repositories.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.SQLException;

public final class Migrator {
    public void migrate() throws SQLException {
        try (var connection = DbAccess.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "migrations/00_Initial.sql"));
        }
    }

    private int getDbVersion() {
        var dbOperations = DbAccess.getDbOperations();
        var tableCount = dbOperations.queryForObject("SELECT COUNT(*) FROM word_types",
                new MapSqlParameterSource(), Integer.class);
        //noinspection ConstantConditions, cannot be null
        if (tableCount == 0) {
            return 0;
        }
        //noinspection ConstantConditions, cannot be null
        return DbAccess.getDbOperations().queryForObject(
                "SELECT version FROM _DataModel", new MapSqlParameterSource(), Integer.class);
    }
}
