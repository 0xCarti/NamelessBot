package commands.bot.misc;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.FlagHandler;

import java.util.List;

public class Help extends CustomCommand {
    public Help() {
        this.name = "Help";
        this.help = "Get some help with commands.";
        this.syntax = "!help <module> [flag]";
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> flags = FlagHandler.getFlags(commandEvent);
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty() && flags.isEmpty()){
            commandEvent.reply(getCategoryHelpEmbed());
            return;
        }

        if(!flags.isEmpty()) {
            if(flags.get(0).equalsIgnoreCase("-h")){
                commandEvent.reply(
                        new EmbedBuilder()
                                .setTitle("Secret Menu")
                                .setDescription("Hey! This flag doesn't do much for the help menu but it does show important info for mostly the music commands. " +
                                        "I didn't really have a convenient place to put that info so glad you found it here. " +
                                        "Have a good day!")
                                .build());
                return;
            }
        }

        String output = "";
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "econ", "economy" -> commandEvent.reply(getCategoryHelpEmbed(MainBot.ECONOMY));
                case "exp" -> commandEvent.reply(getCategoryHelpEmbed(MainBot.EXP));
                case "audio" -> commandEvent.reply(getCategoryHelpEmbed(MainBot.AUDIO));
                case "misc" -> commandEvent.reply(getCategoryHelpEmbed(MainBot.MISC));
                case "admin" -> commandEvent.reply(getCategoryHelpEmbed(MainBot.ADMIN));
                default -> commandEvent.reply("I don't recognize category: " + arg);
            }
        }
        commandEvent.reply(output);
    }

    public MessageEmbed getCategoryHelpEmbed(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Command Categories");
        builder.addField("Admin", "`!help admin`", true);
        builder.addField("Economy", "`!help econ`", true);
        builder.addField("EXP", "`!help exp`", true);
        builder.addField("Audio", "`!help audio`", true);
        builder.addField("Prison", "`!help prison`", true);
        builder.addField("Misc", "`!help misc`", true);
        builder.setFooter("Note: If you don't know how to use a command then add -h after it for the help menu.");
        return builder.build();
    }
    public MessageEmbed getCategoryHelpEmbed(Category category){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(category.getName() + " Commands");
        for (Command command : MainBot.cmdClient.getCommands()) {
            if(command instanceof CustomCommand customCommand && command.getCategory().equals(category) && !command.isHidden()){
                builder.addField("`" + customCommand.getSyntax() + "`", customCommand.getHelp(), false);
            }
        }
        builder.setFooter("For inquiries please contact: Carti#6852");
        return builder.build();
    }
}
