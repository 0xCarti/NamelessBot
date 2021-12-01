package commands.bot.admin;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;
import utilities.Config;

public class Stimulate extends CustomCommand {
    public Stimulate() {
        this.name = "Stimulate";
        this.help = "Stimulate the servers economy.";
        this.syntax = "!stimulate";
        this.category = MainBot.ADMIN;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if(!commandEvent.getMember().isOwner() && !commandEvent.getMember().getId().equals(Config.OWNER_ID)) {
            commandEvent.reply("You do not have permission to use this command.");
            return;
        }

        commandEvent.getMessage().delete().complete();

        try{
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.depositAll(1000);
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
