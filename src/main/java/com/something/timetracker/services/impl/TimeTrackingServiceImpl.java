package com.something.timetracker.services.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.Page;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import com.something.timetracker.services.contracts.TimeTrackingService;

import java.util.Collection;

public class TimeTrackingServiceImpl implements TimeTrackingService {
    private final ProjectRepository projectRepository;

    public TimeTrackingServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void createProject(String projectName) {
        createProjectAndReturn(projectName);
    }

    @Override
    public void startIteration(String projectName) {
        var optionalProject = projectRepository.findByName(projectName);
        Project project = optionalProject.orElseGet(() -> createProjectAndReturn(projectName));
        project.startIteration();
    }

    @Override
    public Collection<Project> getAllProjects() {
        return projectRepository.findAll(new Page(0, Integer.MAX_VALUE));
    }

    private Project createProjectAndReturn(String projectName) {
        Project project = new Project(projectName);
        projectRepository.create(project);
        return project;
    }
}
