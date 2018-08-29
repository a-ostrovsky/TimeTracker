package com.something.timetracker.repositories.impl.proxies;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

public class LoadOnFirstAccessProxySet<T> implements Set<T>, ProxyCollection<T> {

    private final Object syncRoot = new Object();

    private final Supplier<Collection<T>> initializer;
    private final Set<T> addedElements = new HashSet<>();
    private Set<T> backingSet;

    public LoadOnFirstAccessProxySet(Supplier<Collection<T>> initializer) {
        this.initializer = initializer;
    }

    public LoadOnFirstAccessProxySet(Set<T> backingSet) {
        this.initializer = () -> backingSet;
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
        //return backingSet.toArray(a);
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        ensureInitialized();
        boolean added = backingSet.add(t);
        if (added) {
            addedElements.add(t);
        }
        return added;
    }

    @Override
    public boolean remove(Object o) {
        ensureInitialized();
        //return backingSet.remove(o);
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        ensureInitialized();
        return backingSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        ensureInitialized();
        boolean added = false;
        for (T elt : c) {
            boolean elementAdded = add(elt);
            added |= elementAdded;
        }
        return added;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        ensureInitialized();
        throw new UnsupportedOperationException();
        // return backingSet.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        ensureInitialized();
        // return backingSet.removeAll(c);
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        ensureInitialized();
        // backingSet.clear();
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<T> getAddedElements() {
        return addedElements;
    }

    @Override
    public void resetTrackedChanges() {
        synchronized (syncRoot) {
            addedElements.clear();
        }
    }
}
