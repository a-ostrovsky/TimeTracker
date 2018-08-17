package com.something.timetracker.gui.terminal;

import com.something.timetracker.services.contracts.TimeTrackingService;

public class MainViewImpl implements MainView {
    private final MainViewPresenter presenter;

    public MainViewImpl(TimeTrackingService timeTrackingService){
        presenter = new MainViewPresenter(this, timeTrackingService);
    }

    @Override
    public void setText(String projectNamesString) {
        Console.clear();
        System.out.println(projectNamesString);
    }

    @Override
    public void show() {
        presenter.showCurrentProjects();
    }
}
