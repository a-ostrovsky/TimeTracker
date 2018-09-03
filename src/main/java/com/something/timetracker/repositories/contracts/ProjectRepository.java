package com.something.timetracker.repositories.contracts;

import com.something.timetracker.entities.Project;

import java.util.Optional;

public interface ProjectRepository extends GenericRepository<Project> {
    Optional<Project> findByName(String projectName);
    void setActiveProject(Project project);
    Optional<Project> findActiveProject();
}
