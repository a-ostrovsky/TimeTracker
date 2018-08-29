package com.something.timetracker.repositories.impl.proxies;

import org.jetbrains.annotations.Contract;

import java.util.Collection;

public final class ProxyCollectionHelper {
    @Contract("null -> null")
    public static <T> Collection<T> getAddedElements(Collection<T> collection) {
        if (collection instanceof ProxyCollection) {
            //noinspection unchecked
            return  ((ProxyCollection<T>) collection).getAddedElements();
        } else {
            return collection;
        }
    }
}
