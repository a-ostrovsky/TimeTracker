package com.something.timetracker.repositories.hibernateImpl;

import com.something.timetracker.entities.Entity;
import com.something.timetracker.repositories.contracts.GenericRepository;
import com.something.timetracker.repositories.contracts.Page;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BaseDbRepository<T extends Entity> implements GenericRepository<T> {

    private final SessionFactory sessionFactory;

    private final Class<T> type;

    public BaseDbRepository(SessionFactory sessionFactory, Class<T> type) {
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    @Override
    public T findOne(long id) {
        Session session = getCurrentSession();
        return session.get(type, id);
    }

    @Override
    public List<T> findAll(Page page) {
        Session session = getCurrentSession();
        Query<T> query = session.createQuery("from " + type.getName(), type);
        query.setFirstResult(page.getInclusiveStart());
        query.setMaxResults(page.getMaxResults());
        return query.list();
    }

    @Override
    public void create(T entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    @Override
    public void update(T entity) {
        Session session = getCurrentSession();
        session.merge(entity);
    }

    @Override
    public void delete(T entity) {
        Session session = getCurrentSession();
        session.delete(entity);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
