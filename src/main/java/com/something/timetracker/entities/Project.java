package com.something.timetracker.entities;

import com.something.timetracker.system.DateTime;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Project extends Entity {

    private String name;

    private LocalDateTime startTime;

    // Used via reflection in ProjectMapper
    private Set<WorkingTime> workingTimes = new HashSet<>();

    public Project() {
    }

    public Project(String projectName) {
        this();
        setName(projectName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void startIteration() {
        if (startTime == null) {
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
