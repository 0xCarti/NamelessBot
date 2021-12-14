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
        long days = TimeUnit.MILLISECONDS.toDays(now);
        long hours = TimeUnit.MILLISECONDS.toHours(now) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(now));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(now));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(now) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(now));
        commandEvent.reply("Uptime: " + days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
    }
}
