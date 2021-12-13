package commands.bot.admin;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.ServerManager;
import main.MainBot;
import utilities.Config;
import utilities.FlagHandler;

import java.util.List;

public class Disable extends CustomCommand {
    public Disable() {
        this.name = "Disable";
        this.help = "Disable a module for the server.";
        this.syntax = "!disable [all|econ|audio|prison]";
        this.category = MainBot.ADMIN;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        if(!commandEvent.getMember().isOwner() && !commandEvent.getMember().getId().equals(Config.OWNER_ID)) {
            commandEvent.reply("You do not have permission to use this command.");
            return;
        }

        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("Please specify a module to disable.");
            return;
        }

        String module = args.get(0);
        try{
            switch (module.toLowerCase()){
                case "econ" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy = false;
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.exp = false;
                    commandEvent.reply("Economy and exp disabled.");
                }
                case "audio" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio = false;
                    commandEvent.reply("Audio disabled.");
                }
                case "all" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.disableAll();
                    commandEvent.reply("All modules disabled.");
                }
                default -> {
                    commandEvent.reply("Flag not recognized.");
                }

            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
