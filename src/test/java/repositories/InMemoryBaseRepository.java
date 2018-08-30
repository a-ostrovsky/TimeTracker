package repositories;

import com.something.timetracker.entities.Entity;
import com.something.timetracker.repositories.contracts.GenericRepository;
import com.something.timetracker.repositories.contracts.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class InMemoryBaseRepository<T extends Entity> implements GenericRepository<T> {

    private final Map<Long, T> database = new HashMap<>();
    private int currentId = 0;

    protected List<T> findBy(Predicate<T> predicate) {
        return database.values().stream().filter(predicate).collect(toList());
    }

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
    public T create(T entity) {
        entity.setId(currentId++);
        database.put(entity.getId(), entity);
        return entity;
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
