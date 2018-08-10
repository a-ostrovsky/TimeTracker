package com.something.timetracker.services.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.Page;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import com.something.timetracker.services.contracts.TimeTrackingService;

import java.util.Collection;

public class TimeTrackingServiceImpl implements TimeTrackingService {
    private final ProjectRepository projectRepository;

    @Override
    public void createProject(String projectName) {
        Project project = new Project(projectName);
        projectRepository.create(project);
    }

    @Override
    public Collection<Project> getAllProjects() {
        return projectRepository.findAll(new Page(0, Integer.MAX_VALUE));
    }

    public TimeTrackingServiceImpl(ProjectRepository projectRepository){

        this.projectRepository = projectRepository;
    }
}
