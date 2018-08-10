package com.something.timetracker.services.impl.test;

import com.something.timetracker.repositories.contracts.ProjectRepository;
import com.something.timetracker.services.contracts.TimeTrackingService;
import com.something.timetracker.services.impl.TimeTrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.InMemoryProjectRepository;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

class TimeTrackingServiceTest {

    private TimeTrackingService service;

    @BeforeEach
    void setUp(){
        ProjectRepository projectRepository = new InMemoryProjectRepository();
        service = new TimeTrackingServiceImpl(projectRepository);
    }

    @Test
    void added_projects_are_stored(){
        service.createProject("project");
        assertThat(service.getAllProjects(), hasItem(hasProperty("name", is("project"))));
    }

}
