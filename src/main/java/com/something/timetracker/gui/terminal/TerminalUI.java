package com.something.timetracker.gui.terminal;

import com.something.timetracker.construction.ModuleInstaller;

public class TerminalUI {
    public static void main(String[] args) {
        ModuleInstaller installer = new ModuleInstaller();
        installer.createModules();
        installer.getMainUI().show();
    }
}
