package commands.bot.misc;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import main.MainBot;

public class Ping extends CustomCommand {
    public Ping(){
        this.name = "ping";
        this.help = "Pongs the sender!";
        this.syntax = "!ping";
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply(" wanted to get pinged!");
    }
}
