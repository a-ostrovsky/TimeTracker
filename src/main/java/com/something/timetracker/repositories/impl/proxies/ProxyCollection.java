package com.something.timetracker.repositories.impl.proxies;

import java.util.Collection;

public interface ProxyCollection<T> {
    Collection<T> getAddedElements();

    void resetTrackedChanges();
}
