package commands.bot.misc;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.FlagHandler;

import java.util.List;

public class Changelog extends CustomCommand {
    public Changelog() {
        this.name = "Changelog";
        this.help = "Get the most recent changelog for the bot.";
        this.syntax = "!changelog <version-number>";
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }
        commandEvent.reply(getChanges(args.get(0)));
    }

    public MessageEmbed getChanges(String version){
        if(version.equalsIgnoreCase("1.3")){
            return new EmbedBuilder()
                    .setTitle("Changelog for version: 1.3")
                    .addField("Added music functionality", "", false)
                    .addField("Added recording and clipping functionality", "", false)
                    .addField("Removed channel creation functionality due to permission issues", "", false)
                    .addField("Rewrote the different bot functions as modules so, this means server owners can choose to setup only audio on their server or only the economy.", "", false)
                    .addField("Organized save file structure format to better accommodate multiple server save files without mixing them up", "", false)
                    .build();
        }else if(version.equalsIgnoreCase("1.4")){
            return new EmbedBuilder()
                    .setTitle("Changelog for version: 1.4")
                    .addField("Added [flags] for music command syntax's", "", false)
                    .addField("Implemented flag handler object for later use in command syntax's", "", false)
                    .build();
        }else{
            return new EmbedBuilder()
                    .setTitle("Not a valid version.")
                    .setDescription("Unknown")
                    .build();
        }
    }
}
