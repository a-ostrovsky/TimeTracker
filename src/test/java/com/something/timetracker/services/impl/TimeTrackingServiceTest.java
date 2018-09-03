package com.something.timetracker.services.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.services.contracts.TimeTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryProjectRepository;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

class TimeTrackingServiceTest {

    private TimeTrackingService service;
    private InMemoryProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        projectRepository = new InMemoryProjectRepository();
        service = new TimeTrackingServiceImpl(projectRepository);
    }

    @Test
    void added_projects_are_stored() {
        service.createProject("project");
        assertThat(service.getAllProjects(), hasItem(hasProperty("name", is("project"))));
    }

    @Test
    void starting_iteration_for_new_project_creates_that_project() {
        service.startIteration("project");
        Optional<Project> createdProject = projectRepository.findByName("project");
        assertThat(createdProject.isPresent(), is(true));

        assertThat(createdProject.get().getStartOfCurrentIteration().isPresent(), is(true));
    }

    @Test
    void starting_iteration_for_the_project_with_entered_name() {
        service.createProject("project");
        service.startIteration("project");

        Optional<Project> project = projectRepository.findByName("project");
        assertThat(project.isPresent(), is(true));
        assertThat(project.get().getStartOfCurrentIteration().isPresent(), is(true));
    }

    @Test
    void stopping_iteration_stops_the_iteration_of_current_project() {
        service.createProject("project");
        service.startIteration("project");

        service.stopCurrentIteration();

        Optional<Project> project = projectRepository.findByName("project");
        assertThat(project.isPresent(), is(true));
        assertThat(project.get().getWorkingTimes().size(), is(not(0)));
    }

    @Test
    void starting_iteration_when_already_working_on_a_project_stops_the_last_iteration() {
        service.createProject("project_1");
        service.createProject("project_2");
        service.startIteration("project_1");
        service.startIteration("project_2");

        Optional<Project> project = projectRepository.findByName("project_1");
        assertThat(project.isPresent(), is(true));
        assertThat(project.get().getWorkingTimes().size(), is(not(0)));
    }


}
