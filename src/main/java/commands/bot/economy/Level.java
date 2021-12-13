package commands.bot.economy;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.AccountNotFoundException;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;

public class Level extends CustomCommand {
    public Level() {
        this.name = "Level";
        this.help = "Get your current level on the server.";
        this.syntax = "!level";
        this.category = MainBot.EXP;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("EXP is disabled on this server!");
                return;
            }
            commandEvent.reply("Level: " + ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).level);
        }catch (ServerNotFoundException | AccountNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
