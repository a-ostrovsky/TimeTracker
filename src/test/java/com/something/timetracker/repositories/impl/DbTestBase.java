package com.something.timetracker.repositories.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

class DbTestBase {
    private Migrator migrator;

    Migrator getMigrator() {
        return migrator;
    }

    @BeforeEach
    void setUp() throws Exception {
        DbAccess.configure("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        migrator = new Migrator();
    }

    @AfterEach
    void tearDown() {
        // Clear the in-memory database
        DbAccess.getDbOperations().update("SHUTDOWN", new MapSqlParameterSource());
    }
}
