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
        stopCurrentIteration();
        var optionalProject = projectRepository.findByName(projectName);
        Project project = optionalProject.orElseGet(() -> createProjectAndReturn(projectName));
        project.startIteration();
        projectRepository.setActiveProject(project);
    }

    @Override
    public void deleteProject(String projectName) {
        var optionalProject = projectRepository.findByName(projectName);
        optionalProject.ifPresent(projectRepository::delete);
    }

    @Override
    public void stopCurrentIteration() {
        var optionalActiveProject = projectRepository.findActiveProject();
        if (!optionalActiveProject.isPresent()) {
            return;
        }
        Project activeProject = optionalActiveProject.get();
        activeProject.stopIteration();
        projectRepository.update(activeProject);
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
