package com.something.timetracker.gui.terminal;

import com.something.timetracker.services.contracts.TimeTrackingService;

import java.util.Scanner;

public class MainViewImpl implements MainView {
    private final MainViewPresenter presenter;
    private final Scanner scanner;

    public MainViewImpl(TimeTrackingService timeTrackingService) {
        this.presenter = new MainViewPresenter(this, timeTrackingService);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void setText(String projectNamesString) {
        Console.clear();
        System.out.println(projectNamesString);
    }

    @Override
    public void show() {
        presenter.showCurrentProjects();
        processUserInput();
    }

    private void processUserInput() {
        while (true) {
            String input = getUserInput();
            if (input.toUpperCase().startsWith("S")) {
                String projectName = input.substring(1).trim();
                presenter.startIteration(projectName);
            } else if (input.toUpperCase().equals("E")) {
                presenter.endIteration();
            } else if (input.toUpperCase().equals("Q")) {
                System.exit(0);
            }
        }
    }

    private String getUserInput() {
        System.out.println("Actions:");
        System.out.println("S ProjectName: starts iteration (e.g. S project1)");
        System.out.println("E: stops current iteration");
        System.out.println("Q: quits application");
        return scanner.nextLine();
    }
}
