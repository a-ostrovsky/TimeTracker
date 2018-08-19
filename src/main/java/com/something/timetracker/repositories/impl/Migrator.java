package com.something.timetracker.repositories.impl;

import com.something.timetracker.system.Resources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public final class Migrator {
    public void migrate() throws SQLException {
        try (var connection = DbAccess.getConnection()) {
            ensureDataModelTableExists();
            Collection<String> requiredMigrationFiles = calculateMigrations();
            for (String migrationFile : requiredMigrationFiles) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource(migrationFile));
            }
        }
    }

    private Collection<String> calculateMigrations() {
        int dataModelVersion = getDataModelVersion();
        Collection<String> allMigrationFiles = Resources.getResourcesFlat("migrations");
        return getRequiredMigrationFiles(dataModelVersion, allMigrationFiles);
    }

    private Collection<String> getRequiredMigrationFiles(int dataModelVersion,
                                                         Collection<String> migrationFiles) {
        return migrationFiles.stream()
                .sorted()
                .filter(file -> {
                    String fileWithoutPath = Paths.get(file).getFileName().toString();
                    int migrationVersion = Integer.parseInt(fileWithoutPath.split("_")[0]);
                    return migrationVersion > dataModelVersion;
                })
                .collect(toList());
    }

    private void ensureDataModelTableExists() {
        var ops = DbAccess.getDbOperations();
        ops.update("CREATE TABLE IF NOT EXISTS _DataModel (version int);",
                new MapSqlParameterSource());

    }

    private int getDataModelVersion() {
        var ops = DbAccess.getDbOperations();
        @SuppressWarnings("ConstantConditions")
        var isDataModelEmpty = ops.queryForObject("SELECT COUNT (*) FROM _DataModel LIMIT 1",
                new MapSqlParameterSource(), Integer.class) == 0;
        if (isDataModelEmpty) {
            return 0;
        } else {
            //noinspection ConstantConditions
            return DbAccess.getDbOperations().queryForObject(
                    "SELECT version FROM _DataModel", new MapSqlParameterSource(), Integer.class);
        }
    }
}
