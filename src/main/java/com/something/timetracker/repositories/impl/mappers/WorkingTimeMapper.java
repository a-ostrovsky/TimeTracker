package com.something.timetracker.repositories.impl.mappers;

import com.something.timetracker.entities.Project;
import com.something.timetracker.entities.WorkingTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class WorkingTimeMapper {

    @NotNull
    public static SqlParameterSource mapFromWorkingTime(@NotNull Project project,
                                                        @NotNull WorkingTime workingTime) {
        var parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("project_id", project.getId());
        parameterSource.addValue("start", workingTime.getStart());
        parameterSource.addValue("end", workingTime.getEnd());
        return parameterSource;
    }

    @NotNull
    public static WorkingTime mapToWorkingTime(@NotNull ResultSet rs) throws SQLException {
        Timestamp start = rs.getTimestamp("start");
        Timestamp end = rs.getTimestamp("end");
        return new WorkingTime(start.toLocalDateTime(), end.toLocalDateTime());
    }
}