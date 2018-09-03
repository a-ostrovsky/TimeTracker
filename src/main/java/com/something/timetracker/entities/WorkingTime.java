package com.something.timetracker.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class WorkingTime {
    private final LocalDateTime start;

    private final LocalDateTime end;

    public WorkingTime(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingTime that = (WorkingTime) o;
        return Objects.equals(getStart(), that.getStart()) &&
                Objects.equals(getEnd(), that.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd());
    }

    @Override
    public String toString() {
        return "WorkingTime{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
