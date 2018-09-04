package com.something.timetracker.gui.terminal;

import com.something.timetracker.entities.Project;
import com.something.timetracker.entities.WorkingTime;
import com.something.timetracker.services.contracts.TimeTrackingService;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

class MainViewPresenter {
    private final MainView ui;
    private final TimeTrackingService timeTrackingService;

    MainViewPresenter(MainView ui, TimeTrackingService timeTrackingService) {
        this.ui = ui;
        this.timeTrackingService = timeTrackingService;
    }

    @NotNull
    private static String projectsToString(@NotNull Collection<Project> projects) {
        StringBuilder builder = new StringBuilder();
        for (Project project : projects) {
            builder.append(projectToString(project));
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    @NotNull
    private static String projectToString(@NotNull Project project) {
        StringBuilder builder = new StringBuilder();
        builder.append(project.getName()).append(System.lineSeparator());
        for (WorkingTime workingTime : project.getWorkingTimes()) {
            builder.append(" ")
                    .append(workingTime.getStart())
                    .append(" - ")
                    .append(workingTime.getEnd())
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    void showCurrentProjects() {
        Collection<Project> projects = timeTrackingService.getAllProjects();
        String projectsAsString = projectsToString(projects);
        ui.setText(projectsAsString);
    }

    void startIteration(String projectName) {
        timeTrackingService.startIteration(projectName);
        showCurrentProjects();
    }

    void endIteration() {
        timeTrackingService.stopCurrentIteration();
        showCurrentProjects();
    }

    void deleteProject(String projectName) {
        timeTrackingService.deleteProject(projectName);
        showCurrentProjects();
    }
}
