package repositories;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.ProjectRepository;

import java.util.List;
import java.util.Optional;

public class InMemoryProjectRepository
        extends InMemoryBaseRepository<Project> implements ProjectRepository {

    private Project activeProject;

    @Override
    public Optional<Project> findByName(String projectName) {
        List<Project> result = findBy(project -> project.getName().equals(projectName));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public void setActiveProject(Project project) {
        activeProject = project;
    }

    @Override
    public Optional<Project> findActiveProject() {
        return activeProject == null ? Optional.empty() : Optional.of(activeProject);
    }
}
