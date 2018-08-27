package com.something.timetracker.repositories.impl.proxies;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Lazy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

public class LoadOnFirstAccessProxySet<T> implements Set<T> {

    private final Object syncRoot = new Object();

    private final Lazy<Collection<T>> initializer;

    private Set<T> backingSet;

    public LoadOnFirstAccessProxySet(Supplier<Collection<T>> selector) {
        this.initializer = new Lazy<>(selector);
    }

    private void ensureInitialized() {
        synchronized (syncRoot) {
            if (backingSet == null) {
                var backingCollection = initializer.get();
                if (backingCollection instanceof Set) {
                    backingSet = (Set<T>) backingCollection;
                } else {
                    backingSet = new HashSet<>(backingCollection);
                }
            }
        }
    }

    @Override
    public int size() {
        ensureInitialized();
        return backingSet.size();
    }

    @Override
    public boolean isEmpty() {
        ensureInitialized();
        return backingSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        ensureInitialized();
        return backingSet.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        ensureInitialized();
        return backingSet.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        ensureInitialized();
        return backingSet.toArray();

    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        ensureInitialized();
        return backingSet.toArray(a);
    }

    @Override
    public boolean add(T t) {
        ensureInitialized();
        return backingSet.add(t);
    }

    @Override
    public boolean remove(Object o) {
        ensureInitialized();
        return backingSet.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        ensureInitialized();
        return backingSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        ensureInitialized();
        return backingSet.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        ensureInitialized();
        return backingSet.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        ensureInitialized();
        return backingSet.removeAll(c);
    }

    @Override
    public void clear() {
        ensureInitialized();
        backingSet.clear();
    }
}
