package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ProjectDbRepositoryTest extends DbTestBase {

    @BeforeEach
    @Override
    void setUp() throws Exception {
        super.setUp();
        getMigrator().migrate();
    }

    @Test
    void can_crud_projects() {
        var repository = new ProjectDbRepository();
        var project = new Project("test_project");

        // Create
        project = repository.create(project);

        // Read
        assertThat(repository.findOne(project.getId()), not(nullValue()));
        assertThat(repository.findAll(new Page(0, 42)), hasItem(project));

        // Update
        project.setName("new_name");
        repository.update(project);
        assertThat(repository.findOne(project.getId()).getName(), equalTo("new_name"));

        // Delete
        repository.delete(project);
        assertThat(repository.findOne(project.getId()), is(nullValue()));
    }
}
