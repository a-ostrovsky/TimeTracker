package com.something.timetracker.repositories.contracts;

import com.something.timetracker.entities.Entity;

import java.util.List;

public interface GenericRepository<T extends Entity> {
    T findOne(long id); //TODO: Should be Optional
    List<T> findAll(Page page);
    T create(T entity);
    void update(T entity);
    void delete(T entity);
}
