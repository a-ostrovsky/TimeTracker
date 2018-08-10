package com.something.timetracker.repositories.hibernateImpl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import org.hibernate.SessionFactory;

public class ProjectDbRepository extends BaseDbRepository<Project>
        implements ProjectRepository {
    public ProjectDbRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Project.class);
    }


}
