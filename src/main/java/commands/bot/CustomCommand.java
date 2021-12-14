package commands.bot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import utilities.Config;
import utilities.Logger;

public abstract class CustomCommand extends Command {
    protected String syntax = "null";

    @Override
    protected void execute(CommandEvent commandEvent) {
        Logger.debug(1, "Executing [][] command on [] for [].", Config.PREFIX, getName(), commandEvent.getGuild().getName(), commandEvent.getMember().getUser().getAsTag());
    }

    public String getSyntax() {
        return syntax;
    }
}
