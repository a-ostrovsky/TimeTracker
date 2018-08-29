package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.Page;
import helpers.TimeMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ProjectDbRepositoryTest extends DbTestBase {

    @BeforeEach
    @Override
    void setUp() throws Exception {
        super.setUp();
        getMigrationRunner().migrate();
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

    @Test
    void can_crud_working_times_of_projects() {
        var repository = new ProjectDbRepository();
        var project = new Project("test_project");

        TimeMachine.use();
        project.startIteration();
        TimeMachine.addTime(Duration.ofHours(1));
        project.stopIteration();

        TimeMachine.addTime(Duration.ofHours(1));
        project.startIteration();
        TimeMachine.addTime(Duration.ofHours(1));
        project.stopIteration();

        // Create
        repository.create(project);

        // Read
        var createdProject = repository.findOne(project.getId());
        assertThat(createdProject.getWorkingTimes().size(), equalTo(2));

        // Work again
        TimeMachine.addTime(Duration.ofHours(1));
        createdProject.startIteration();
        TimeMachine.addTime(Duration.ofHours(1));
        createdProject.stopIteration();

        // Update
        repository.update(createdProject);

        // Read again
        var updatedProject = repository.findOne(project.getId());
        assertThat(updatedProject.getWorkingTimes().size(), equalTo(3));
    }
}
