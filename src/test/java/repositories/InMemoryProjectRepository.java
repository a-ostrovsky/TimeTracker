package repositories;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.ProjectRepository;

public class InMemoryProjectRepository
        extends InMemoryBaseRepository<Project> implements ProjectRepository {
}
