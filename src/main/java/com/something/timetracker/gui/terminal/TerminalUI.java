package com.something.timetracker.gui.terminal;

import com.something.timetracker.construction.ModuleInstaller;

public class TerminalUI {
    public static void main(String[] args) throws Exception{
        ModuleInstaller installer = new ModuleInstaller();
        installer.createModules();
        installer.getMigrationRunner().migrate();
        installer.getMainUI().show();
    }
}
