package commands.console;

import main.MainBot;
import utilities.Config;
import utilities.Logger;

public class PrefixConsoleCommand extends ConsoleCommand {
    public PrefixConsoleCommand(){
        this.name = "Prefix";
        this.description = "Changes the prefix of the bot.";
        this.syntax = "/prefix <prefix>";
    }

    @Override
    public void execute() {
        Logger.log(1, "Prefix is [[]].", MainBot.cmdClient.getPrefix());
    }

    @Override
    public void execute(String[] args) {
        String prefix = args[0].charAt(0) + "";
        Config.PREFIX = prefix;
        Logger.log(1, "Prefix set to [[]].", MainBot.cmdClient.getPrefix());
    }
}
