package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.entities.WorkingTime;
import com.something.timetracker.repositories.contracts.Page;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import com.something.timetracker.repositories.impl.mappers.CurrentStartOfIteration;
import com.something.timetracker.repositories.impl.mappers.ProjectMapper;
import com.something.timetracker.repositories.impl.mappers.ProjectMappingConfiguration;
import com.something.timetracker.repositories.impl.mappers.WorkingTimeMapper;
import com.something.timetracker.repositories.impl.proxies.LoadOnFirstAccessProxySet;
import com.something.timetracker.repositories.impl.proxies.ProxyCollection;
import com.something.timetracker.repositories.impl.proxies.ProxyCollectionHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.time.LocalDateTime;
import java.util.*;

//TODO: Transactions
public class ProjectDbRepository extends DbRepository<Project> implements ProjectRepository {

    @Override
    public Project findOne(long id) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        var iterationStart = new CurrentStartOfIteration();
        var results = ops.query("SELECT * FROM Projects WHERE id = :id", parameterSource,
                (rs, rowNumber) -> ProjectMapper.mapToProject(rs, findWorkingTimes(id),
                        iterationStart.getStartOfCurrentIteration(id)));
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Project> findAll(Page page) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("offset", page.getInclusiveStart());
        parameterSource.addValue("limit", page.getMaxResults());
        var iterationStart = new CurrentStartOfIteration();
        return ops.query("SELECT * FROM Projects LIMIT :limit OFFSET :offset",
                parameterSource, (rs, rowNum) -> {
                    long id = rs.getLong("id");
                    Set<WorkingTime> workingTimes = findWorkingTimes(id);
                    var startTimeOfIteration = iterationStart.getStartOfCurrentIteration(id);
                    return ProjectMapper.mapToProject(rs, workingTimes, startTimeOfIteration);
                });
    }

    @Override
    public Project create(Project entity) {
        var ops = DbAccess.getDbOperations();
        var keyholder = new GeneratedKeyHolder();
        var parameterSource = ProjectMapper.mapFromProject(entity,
                ProjectMappingConfiguration.DO_NOT_MAP_ID);
        ops.update("INSERT INTO Projects (name) VALUES (:name)", parameterSource, keyholder);
        entity.setId(Objects.requireNonNull(keyholder.getKey()).longValue());
        insertWorkingTimes(entity);
        ensureWorkingTimeChangesAreTracked(entity);
        return entity;
    }

    @Override
    public void update(Project entity) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = ProjectMapper.mapFromProject(entity,
                ProjectMappingConfiguration.MAP_ALL);
        ops.update("UPDATE Projects SET name = :name WHERE id = :id", parameterSource);
        insertWorkingTimes(entity);
        ensureWorkingTimeChangesAreTracked(entity);
        if (!entity.getStartOfCurrentIteration().isPresent()) {
            ops.update("DELETE FROM CurrentIteration WHERE project_id = :id", parameterSource);
        }
    }

    @Override
    public void delete(Project entity) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = ProjectMapper.mapFromProject(entity,
                ProjectMappingConfiguration.MAP_ALL);
        ops.update("DELETE FROM Projects WHERE id = :id", parameterSource);
    }

    private void ensureWorkingTimeChangesAreTracked(@NotNull Project entity) {
        if (entity.getWorkingTimes() instanceof ProxyCollection) {
            ((ProxyCollection) entity.getWorkingTimes()).resetTrackedChanges();
        } else {
            ProjectMapper.setWorkingTimes(entity,
                    new LoadOnFirstAccessProxySet<>(entity.getWorkingTimes()));
        }
    }

    @NotNull
    @Contract("_ -> new")
    private Set<WorkingTime> findWorkingTimes(long projectsId) {
        return new LoadOnFirstAccessProxySet<>(() -> {
            var ops = DbAccess.getDbOperations();
            var parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id", projectsId);

            var workingTimes = new HashSet<WorkingTime>();
            ops.query("SELECT * FROM WorkingTimes WHERE project_id = :id",
                    parameterSource, rs -> {
                        var workingTime = WorkingTimeMapper.mapToWorkingTime(rs);
                        workingTimes.add(workingTime);
                    });
            return workingTimes;
        });
    }

    private void insertWorkingTimes(@NotNull Project project) {
        Collection<WorkingTime> addedElements =
                ProxyCollectionHelper.getAddedElements(project.getWorkingTimes());
        for (WorkingTime workingTime : addedElements) {
            var ops = DbAccess.getDbOperations();
            var parameterSource = WorkingTimeMapper.mapFromWorkingTime(project, workingTime);
            ops.update("INSERT INTO WorkingTimes (project_id, start, end) " +
                    "VALUES (:project_id, :start, :end)", parameterSource);
        }
    }

    @Override
    public Optional<Project> findByName(String projectName) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", projectName);
        var startTime = new CurrentStartOfIteration();
        var results = ops.query("SELECT * FROM Projects WHERE name = :name", parameterSource,
                (rs, rowNumber) -> {
                    long id = rs.getLong("id");
                    var startTimeOfIteration = startTime.getStartOfCurrentIteration(id);
                    return ProjectMapper.mapToProject(rs, findWorkingTimes(id),
                            startTimeOfIteration);
                });
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public void setActiveProject(Project project) {
        project = createIfNotExists(project);
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        Optional<LocalDateTime> startTime = project.getStartOfCurrentIteration();
        if (!startTime.isPresent()) {
            throw new NoActiveIterationException(project);
        }
        parameterSource.addValue("project_id", project.getId());
        parameterSource.addValue("start", startTime.get());
        ops.update("INSERT INTO CurrentIteration (project_id, start) " +
                "VALUES (:project_id, :start)", parameterSource);

    }

    @Override
    public Optional<Project> findActiveProject() {
        var iterationStart = new CurrentStartOfIteration();
        Optional<Long> projectId = iterationStart.findActiveProjectId();
        return projectId.map(this::findOne);
    }

    private class NoActiveIterationException extends RuntimeException {
        NoActiveIterationException(@NotNull Project project) {
            super("Project " + project.getName() + " has no active iteration");
        }
    }

}
