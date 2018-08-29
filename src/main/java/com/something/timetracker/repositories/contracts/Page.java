package com.something.timetracker.repositories.contracts;

public class Page {
    private final int inclusiveStart;
    private final int maxResults;

    public Page(int inclusiveStart, int maxResults) {
        this.inclusiveStart = inclusiveStart;
        this.maxResults = maxResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getInclusiveStart() {
        return inclusiveStart;
    }
}
