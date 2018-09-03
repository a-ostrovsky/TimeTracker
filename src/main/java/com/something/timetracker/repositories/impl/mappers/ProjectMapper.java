package com.something.timetracker.repositories.impl.mappers;

import com.something.timetracker.entities.Project;
import com.something.timetracker.entities.WorkingTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

public final class ProjectMapper {
    public static SqlParameterSource mapFromProject(@NotNull Project project,
                                                    @NotNull ProjectMappingConfiguration configuration) {
        var parameterSource = new MapSqlParameterSource();
        if (configuration == ProjectMappingConfiguration.MAP_ALL) {
            parameterSource.addValue("id", project.getId());
        }
        parameterSource.addValue("name", project.getName());
        return parameterSource;
    }

    public static Project mapToProject(@NotNull ResultSet rs, Set<WorkingTime> workingTimes,
                                       LocalDateTime startTime) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        setWorkingTimes(project, workingTimes);
        setStartTime(project, startTime);
        return project;
    }

    public static void setWorkingTimes(Project project, Set<WorkingTime> workingTimes) {
        try {
            Field workingTimesField = Project.class.getDeclaredField("workingTimes");
            workingTimesField.setAccessible(true);
            workingTimesField.set(project, workingTimes);
        } catch (Exception e) {
            throw new MappingException("Failed to map Project", e);
        }
    }

    public static void setStartTime(Project project, LocalDateTime startTime) {
        if (startTime == null) {
            return;
        }
        try {
            Field startTimeField = Project.class.getDeclaredField("startTime");
            startTimeField.setAccessible(true);
            startTimeField.set(project, startTime);
        } catch (Exception e) {
            throw new MappingException("Failed to map Project", e);
        }
    }
}
