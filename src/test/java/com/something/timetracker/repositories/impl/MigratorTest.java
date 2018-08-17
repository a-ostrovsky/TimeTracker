package com.something.timetracker.repositories.impl;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class MigratorTest extends DbTestBase {
    @Test
    void creates_initial_configuration_if_none_exists() throws Exception {
        getMigrator().migrate();
        Integer version = DbAccess.getDbOperations().queryForObject(
                "SELECT version FROM _DataModel", new MapSqlParameterSource(), Integer.class);
        assertThat(version, equalTo(1));
    }
}
