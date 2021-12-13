package commands.console;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import main.MainBot;
import net.dv8tion.jda.api.entities.Guild;
import utilities.Logger;

public class CountConsoleCommand extends ConsoleCommand {
    public CountConsoleCommand() {
        this.name = "Count";
        this.description = "Get the number of servers and users the bot is in.";
        this.syntax = "/count";
    }

    @Override
    public void execute() {
        int servers = MainBot.builder.getGuilds().size();
        int users = 0;
        for(Guild guild : MainBot.builder.getGuilds()) {
            users += guild.getMemberCache().size();
        }
        Logger.debug("Servers: []\tUsers: []", servers, users);
    }

    @Override
    public void execute(String[] args) {
        execute();
    }
}
