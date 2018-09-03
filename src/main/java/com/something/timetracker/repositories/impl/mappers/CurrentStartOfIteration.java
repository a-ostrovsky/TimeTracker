package com.something.timetracker.repositories.impl.mappers;

import com.something.timetracker.repositories.impl.DbAccess;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.LocalDateTime;
import java.util.Optional;

public class CurrentStartOfIteration {

    private final Object syncRoot = new Object();

    private ActiveProjectIdWithStartOfIteration activeProjectAndStartOfIteration;

    private boolean isLoaded = false;

    private void load() {
        this.activeProjectAndStartOfIteration = findActiveProjectIdWithStartOfIteration();
    }

    public Optional<Long> findActiveProjectId() {
        synchronized (syncRoot) {
            ensureLoaded();

        }
        if (activeProjectAndStartOfIteration == null) {
            return Optional.empty();
        }
        return Optional.of(activeProjectAndStartOfIteration.ProjectId);
    }

    @Nullable
    public LocalDateTime getStartOfCurrentIteration(long projectId) {
        synchronized (syncRoot) {
            ensureLoaded();
            if (activeProjectAndStartOfIteration == null) {
                return null;
            }
            if (projectId != activeProjectAndStartOfIteration.ProjectId) {
                return null;
            }
            return activeProjectAndStartOfIteration.StartOfIteration;
        }
    }

    private void ensureLoaded() {
        if (!isLoaded) {
            load();
            isLoaded = true;
        }
    }

    @Nullable
    private ActiveProjectIdWithStartOfIteration findActiveProjectIdWithStartOfIteration() {
        try {
            var ops = DbAccess.getDbOperations();
            return ops.queryForObject(
                    "SELECT * FROM CurrentIteration", new MapSqlParameterSource(),
                    (rs, rowNum) -> new ActiveProjectIdWithStartOfIteration(
                            rs.getLong("project_id"),
                            rs.getTimestamp("start").toLocalDateTime()));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private class ActiveProjectIdWithStartOfIteration {
        final long ProjectId;
        final LocalDateTime StartOfIteration;

        ActiveProjectIdWithStartOfIteration(long projectId, LocalDateTime startOfIteration) {
            this.ProjectId = projectId;
            this.StartOfIteration = startOfIteration;
        }
    }
}
