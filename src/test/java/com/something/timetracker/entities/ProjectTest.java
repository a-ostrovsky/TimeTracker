package com.something.timetracker.entities;

import com.google.common.collect.Sets;
import com.something.timetracker.system.DateTime;
import helpers.TimeMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class ProjectTest {

    @BeforeEach
    public void setUp() {
        TimeMachine.use();
    }

    @Test
    public void starting_tracking_starts_tracking() {
        LocalDateTime time = DateTime.now();

        Project project = new Project();
        project.startIteration();

        assertThat(project.getStartOfCurrentIteration(), equalTo(Optional.of(time)));
    }

    @Test
    public void stopping_tracking_stops_tracking() {
        Project project = new Project();

        LocalDateTime startTime = DateTime.now();
        project.startIteration();

        LocalDateTime endTime = TimeMachine.addTime(Duration.ofHours(1));
        project.stopIteration();

        assertThat(project.getStartOfCurrentIteration(), equalTo(Optional.empty()));
        assertThat(project.getWorkingTimes(),
                equalTo(Sets.newHashSet(new WorkingTime(startTime, endTime))));
    }

    @Test
    public void working_time_history_is_stored() {
        Project project = new Project();

        LocalDateTime startTimeOfFirstIteration = DateTime.now();
        project.startIteration();
        LocalDateTime endTimeOfFirstIteration = TimeMachine.addTime(Duration.ofHours(1));
        project.stopIteration();

        LocalDateTime startTimeOfSecondIteration = TimeMachine.addTime(Duration.ofHours(1));
        project.startIteration();
        LocalDateTime endTimeOfSecondIteration = TimeMachine.addTime(Duration.ofHours(1));
        project.stopIteration();

        assertThat(project.getStartOfCurrentIteration(), equalTo(Optional.empty()));
        assertThat(project.getWorkingTimes(), equalTo(Sets.newHashSet(
                new WorkingTime(startTimeOfFirstIteration, endTimeOfFirstIteration),
                new WorkingTime(startTimeOfSecondIteration, endTimeOfSecondIteration))));
    }

    @Test
    public void cannot_start_active_iteration_again() {
        Project project = new Project();
        LocalDateTime startTime = DateTime.now();
        project.startIteration();
        TimeMachine.addTime(Duration.ofHours(42));
        assertThat(project.getStartOfCurrentIteration(), equalTo(Optional.of(startTime)));
    }
}