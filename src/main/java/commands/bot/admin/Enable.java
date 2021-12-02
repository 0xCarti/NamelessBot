package commands.bot.admin;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.OptionManager;
import main.managers.ServerManager;
import main.MainBot;
import utilities.Config;
import utilities.FlagHandler;

import java.util.List;

public class Enable extends CustomCommand {
    public Enable() {
        this.name = "Enable";
        this.help = "Enable modules for the server.";
        this.syntax = "!enable [all|econ|audio|prison]";
        this.category = MainBot.ADMIN;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if(!commandEvent.getMember().isOwner() && !commandEvent.getMember().getId().equals(Config.OWNER_ID)) {
            commandEvent.reply("You do not have permission to use this command.");
            return;
        }

        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("Please specify a module to setup.");
            return;
        }

        String module = args.get(0);
        try{
            switch (module.toLowerCase()){
                case "econ" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy = true;
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.exp = true;
                    commandEvent.reply("Economy and exp enabled.");
                }
                case "audio" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio = true;
                    commandEvent.reply("Audio enabled.");
                }
                case "steam" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.steam = true;
                    commandEvent.reply("Steam enabled.");
                }
                case "all" -> {
                    ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.enableAll();
                    commandEvent.reply("All modules enabled.");
                }
                default -> {
                    commandEvent.reply("Flag not recognized.");
                }

            }
            ServerManager.save();
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }


}
