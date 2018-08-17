package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Project;
import com.something.timetracker.repositories.contracts.Page;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ProjectDbRepository implements ProjectRepository {

    private static SqlParameterSource mapFromProject(Project project,
                                                     MappingConfiguration configuration) {
        var parameterSource = new MapSqlParameterSource();
        if (configuration == MappingConfiguration.MAP_ALL) {
            parameterSource.addValue("id", project.getId());
        }
        parameterSource.addValue("name", project.getName());
        return parameterSource;
    }

    private Project mapToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        return project;
    }

    @Override
    public Project findOne(long id) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        var results = ops.query("SELECT * FROM Projects WHERE id = :id", parameterSource,
                (rs, rowNumber) -> mapToProject(rs));
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Project> findAll(Page page) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("offset", page.getInclusiveStart());
        parameterSource.addValue("limit", page.getMaxResults());
        return ops.query("SELECT * FROM Projects LIMIT :limit OFFSET :offset",
                parameterSource, (rs, rowNumber) -> mapToProject(rs));
    }

    @Override
    public Project create(Project entity) {
        var ops = DbAccess.getDbOperations();
        var keyholder = new GeneratedKeyHolder();
        var parameterSource = mapFromProject(entity, MappingConfiguration.DO_NOT_MAP_ID);
        ops.update("INSERT INTO Projects(name) VALUES (:name)", parameterSource, keyholder);
        entity.setId(Objects.requireNonNull(keyholder.getKey()).longValue());
        return entity;
    }

    @Override
    public void update(Project entity) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = mapFromProject(entity, MappingConfiguration.MAP_ALL);
        ops.update("UPDATE Projects SET name = :name WHERE id = :id", parameterSource);
    }

    @Override
    public void delete(Project entity) {
        var ops = DbAccess.getDbOperations();
        var parameterSource = mapFromProject(entity, MappingConfiguration.MAP_ALL);
        ops.update("DELETE FROM Projects WHERE id = :id", parameterSource);
    }

    private enum MappingConfiguration {MAP_ALL, DO_NOT_MAP_ID}
}
