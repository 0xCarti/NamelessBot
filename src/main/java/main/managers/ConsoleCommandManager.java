package main.managers;

import commands.console.ConsoleCommand;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCommandManager {
    private static final List<ConsoleCommand> commands = new ArrayList<>();

    public static void registerCommand(ConsoleCommand command) {
        commands.add(command);
    }

    public static void executeCommand(String command) {
        for (ConsoleCommand consoleCommand : commands) {
            if (consoleCommand.name.equalsIgnoreCase(command)) {
                consoleCommand.execute();
                return;
            }
        }
    }

    public static void executeCommand(String command, String[] args) {
        for (ConsoleCommand consoleCommand : commands) {
            if (consoleCommand.name.equalsIgnoreCase(command)) {
                consoleCommand.execute(args);
                return;
            }
        }
    }
}
