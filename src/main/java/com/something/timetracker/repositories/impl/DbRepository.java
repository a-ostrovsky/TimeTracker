package com.something.timetracker.repositories.impl;

import com.something.timetracker.entities.Entity;
import com.something.timetracker.repositories.contracts.GenericRepository;
import org.jetbrains.annotations.NotNull;

abstract class DbRepository<T extends Entity> implements GenericRepository<T> {

    @NotNull T createIfNotExists(@NotNull T entity) {
        if (entity.getId() == 0) {
            return create(entity);
        }
        return entity;
    }
}
