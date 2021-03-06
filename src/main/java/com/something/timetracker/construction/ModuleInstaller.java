package com.something.timetracker.construction;

import com.something.timetracker.gui.UI;
import com.something.timetracker.gui.terminal.MainViewImpl;
import com.something.timetracker.repositories.contracts.ProjectRepository;
import com.something.timetracker.repositories.impl.DbAccess;
import com.something.timetracker.repositories.impl.MigrationRunner;
import com.something.timetracker.repositories.impl.ProjectDbRepository;
import com.something.timetracker.services.contracts.TimeTrackingService;
import com.something.timetracker.services.impl.TimeTrackingServiceImpl;

public class ModuleInstaller {

    private MainViewImpl mainUI;
    private MigrationRunner migrationRunner;

    public void createModules() {
        DbAccess.configureDefault();
        ProjectRepository projectRepository = new ProjectDbRepository();
        migrationRunner = new MigrationRunner();
        TimeTrackingService timeTrackingService = new TimeTrackingServiceImpl(projectRepository);
        mainUI = new MainViewImpl(timeTrackingService);
    }

    public UI getMainUI() {
        return mainUI;
    }

    public MigrationRunner getMigrationRunner(){
        return migrationRunner;
    }
}
