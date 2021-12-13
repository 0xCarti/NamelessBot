package commands.bot.misc;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;

import java.io.IOException;
import java.util.Properties;

public class Version extends CustomCommand {
    final Properties properties = new Properties();

    public Version() {
        this.name = "Version";
        this.help = "Get the current version of the bot.";
        this.syntax = "!version";
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("project.properties"));
            commandEvent.reply("Version: " + properties.getProperty("version="));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
