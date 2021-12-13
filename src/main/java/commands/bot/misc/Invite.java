package commands.bot.misc;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;
import utilities.Config;

public class Invite extends CustomCommand {
    public Invite() {
        this.name = "invite";
        this.help = "Replies with an invite link.\r\n\tSyntax: !invite";
        this.syntax = "!invite";
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        commandEvent.reply(Config.INVITE);
    }
}
