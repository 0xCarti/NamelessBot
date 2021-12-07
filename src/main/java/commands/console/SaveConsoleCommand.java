package commands.console;

import main.managers.ServerManager;
import utilities.Logger;

public class SaveConsoleCommand extends ConsoleCommand {

    public SaveConsoleCommand() {
        this.name = "Save";
        this.description = "Saves the servers of the bot.";
        this.syntax = "/save";
    }

    @Override
    public void execute() {
        Logger.debug(2, "Saving servers...");
        ServerManager.save();
        Logger.debug(2, "Saved servers.");
    }

    @Override
    public void execute(String[] args) {
        execute();
    }
}
