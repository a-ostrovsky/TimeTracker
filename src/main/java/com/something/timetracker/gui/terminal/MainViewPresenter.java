package com.something.timetracker.gui.terminal;

import com.google.common.base.Joiner;
import com.something.timetracker.entities.Project;
import com.something.timetracker.services.contracts.TimeTrackingService;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

class MainViewPresenter {
    private final MainView ui;
    private final TimeTrackingService timeTrackingService;

    MainViewPresenter(MainView ui, TimeTrackingService timeTrackingService) {
        this.ui = ui;
        this.timeTrackingService = timeTrackingService;
    }

    void showCurrentProjects() {
        Collection<Project> projects = timeTrackingService.getAllProjects();
        final List<String> projectNames =
                projects.stream().map(Project::getName).collect(toList());
        final String projectNamesString = Joiner.on(System.lineSeparator()).join(projectNames);
        ui.setText(projectNamesString);
    }
}
