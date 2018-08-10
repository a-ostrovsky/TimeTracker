package repositories;

import com.something.timetracker.entities.Entity;
import com.something.timetracker.repositories.contracts.GenericRepository;
import com.something.timetracker.repositories.contracts.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryBaseRepository<T extends Entity> implements GenericRepository<T> {

    private final Map<Long, T> database = new HashMap<>();

    @Override
    public T findOne(long id) {
        return database.get(id);
    }

    @Override
    public List<T> findAll(Page page) {
        ArrayList<T> values = new ArrayList<>(database.values());
        int start = page.getInclusiveStart();
        int end = Math.min(start + page.getMaxResults(), values.size());
        return values.subList(start, end);
    }

    @Override
    public void create(T entity) {
        database.put(entity.getId(), entity);
    }

    @Override
    public void update(T entity) {
        database.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        database.remove(entity.getId());
    }
}
