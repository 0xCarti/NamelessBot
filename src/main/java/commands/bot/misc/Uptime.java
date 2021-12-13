package commands.bot.misc;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;
import utilities.Config;

import java.util.concurrent.TimeUnit;

public class Uptime extends CustomCommand {
    public Uptime() {
        this.name = "Uptime";
        this.help = "Get the uptime of the bot";
        this.syntax = Config.PREFIX + this.name;
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        long now = System.currentTimeMillis() - MainBot.start;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(now);
        long minutes = seconds/60;
        seconds = seconds%60;
        commandEvent.reply("I've been awake for " + minutes + " minutes and " + seconds + " seconds");
    }
}
