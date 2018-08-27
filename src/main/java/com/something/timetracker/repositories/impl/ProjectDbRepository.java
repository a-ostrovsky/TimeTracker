package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.entities.WorkingTime;
import com.something.timetracker.repositories.contracts.Page;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import com.something.timetracker.repositories.impl.mappers.ProjectMapper;
import com.something.timetracker.repositories.impl.mappers.ProjectMappingConfiguration;
import com.something.timetracker.repositories.impl.mappers.WorkingTimeMapper;
import com.something.timetracker.repositories.impl.proxies.LoadOnFirstAccessProxySet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProjectDbRepository implements ProjectRepository {

    @Override
    public Project findOne(long id) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        var results = ops.query("SELECT * FROM Projects WHERE id = :id", parameterSource,
                (rs, rowNumber) -> ProjectMapper.mapToProject(rs, findWorkingTimes(id)));
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Project> findAll(Page page) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("offset", page.getInclusiveStart());
        parameterSource.addValue("limit", page.getMaxResults());
        return ops.query("SELECT * FROM Projects LIMIT :limit OFFSET :offset",
                parameterSource, (rs, rowNum) -> {
                    Set<WorkingTime> workingTimes = findWorkingTimes(rs.getLong("id"));
                    return ProjectMapper.mapToProject(rs, workingTimes);
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
        return entity;
    }

    @Override
    public void update(Project entity) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = ProjectMapper.mapFromProject(entity,
                ProjectMappingConfiguration.MAP_ALL);
        ops.update("UPDATE Projects SET name = :name WHERE id = :id", parameterSource);
    }

    @Override
    public void delete(Project entity) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = ProjectMapper.mapFromProject(entity,
                ProjectMappingConfiguration.MAP_ALL);
        ops.update("DELETE FROM Projects WHERE id = :id", parameterSource);
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

    private void insertWorkingTimes(Project project) {
        Set<WorkingTime> workingTimes = project.getWorkingTimes();
        for (WorkingTime workingTime : workingTimes) {
            var ops = DbAccess.getDbOperations();
            var parameterSource = WorkingTimeMapper.mapFromWorkingTime(project, workingTime);
            ops.update("INSERT INTO WorkingTimes (project_id, start, end) " +
                    "VALUES (:project_id, :start, :end)", parameterSource);
        }
    }
}
