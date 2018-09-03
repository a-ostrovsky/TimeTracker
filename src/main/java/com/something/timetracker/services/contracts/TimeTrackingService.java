package com.something.timetracker.services.contracts;

import com.something.timetracker.entities.Project;

import java.util.Collection;

public interface TimeTrackingService {
    void createProject(String projectName);
    void startIteration(String projectName);
    void stopCurrentIteration();
    Collection<Project> getAllProjects();
}
