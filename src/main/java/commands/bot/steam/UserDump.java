package commands.bot.steam;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;
import utilities.Config;

public class UserDump extends CustomCommand {
    public UserDump() {
        this.name = "UserDump";
        this.help = "Gets a dump file of information about a steam user's progress in a game.";
        this.syntax = Config.PREFIX + this.name + " <gameID> <steamID|URL>";
        this.category = MainBot.STEAM;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        //Do code.
    }
}
