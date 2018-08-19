package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Project;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class MigratorTest extends DbTestBase {
    @Test
    void creates_initial_configuration_if_none_exists() throws Exception {
        getMigrator().migrate();
        Integer version = DbAccess.getDbOperations().queryForObject(
                "SELECT version FROM _DataModel", new MapSqlParameterSource(), Integer.class);
        assertThat(version, equalTo(1));
    }

    @Test
    void existing_data_is_not_lost() throws Exception {
        getMigrator().migrate();
        var repository = new ProjectDbRepository();
        var project = new Project("test_project");
        project = repository.create(project);

        getMigrator().migrate();
        assertThat(repository.findOne(project.getId()), notNullValue());
    }
}
