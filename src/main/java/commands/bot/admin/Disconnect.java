package commands.bot.admin;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;
import utilities.Config;
import utilities.Logger;

public class Disconnect extends CustomCommand {
    public Disconnect() {
        this.name = "Disconnect";
        this.help = "Deletes server save file and makes the bot leave.";
        this.syntax = "!disconnect";
        this.category = MainBot.ADMIN;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if(!commandEvent.getMember().isOwner() && !commandEvent.getMember().getId().equals(Config.OWNER_ID)) {
            commandEvent.reply("You do not have permission to use this command.");
            return;
        }

        Logger.debug(1, "Starting disconnect process for [].", commandEvent.getGuild().getName());
        try{
            ServerManager.findServer(commandEvent.getGuild().getId()).audioManager.removeAll();
            Logger.debug(1, "\tAudio Disconnected and files deleted.", commandEvent.getGuild().getName());
            ServerManager.remove(commandEvent.getGuild().getId());
            Logger.debug(1, "Finished disconnecting from [].", commandEvent.getGuild().getName());
            commandEvent.getGuild().leave().complete();
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
            Logger.debug(3, e.getMessage());
        }
    }

}
