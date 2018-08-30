package com.something.timetracker.services.impl;

import com.something.timetracker.services.contracts.TimeTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryProjectRepository;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
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
        var createdProject = projectRepository.findByName("project");
        assertThat(createdProject.isPresent(), is(true));

        assertThat(createdProject.get().getStartOfCurrentIteration().isPresent(), is(true));
    }

    @Test
    void starting_iteration_iteration_for_the_project_with_entered_name() {
        service.createProject("project");
        service.startIteration("project");

        var project = projectRepository.findByName("project");
        assertThat(project.isPresent(), is(true));
        assertThat(project.get().getStartOfCurrentIteration().isPresent(), is(true));
    }


}
