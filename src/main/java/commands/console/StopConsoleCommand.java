package commands.console;

import commands.ConsoleCommand;
import main.managers.ServerManager;
import utilities.Logger;

public class StopConsoleCommand extends ConsoleCommand {
    public StopConsoleCommand() {
        this.name = "stop";
        this.description = "Stops the bot.";
        this.syntax = "/stop";
    }

    @Override
    public void execute() {
        Logger.debug(1, "Shutting down, saving servers.");
        ServerManager.save();
        System.exit(0);
    }

    @Override
    public void execute(String[] args) {
        execute();
    }
}
