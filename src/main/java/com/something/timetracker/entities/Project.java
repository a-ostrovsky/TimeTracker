package com.something.timetracker.entities;

import com.something.timetracker.system.DateTime;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Project {
    private long id;

    private String name;

    private LocalDateTime startTime;

    private Set<WorkingTime> workingTimes = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void startIteration() {
        if(startTime == null){
            startTime = DateTime.now();
        }
    }

    public void stopIteration() {
        workingTimes.add(new WorkingTime(startTime, DateTime.now()));
        startTime = null;
    }

    public Optional<LocalDateTime> getStartOfCurrentIteration() {
        return Optional.ofNullable(startTime);
    }

    public Set<WorkingTime> getWorkingTimes() {
        return workingTimes;
    }
}
