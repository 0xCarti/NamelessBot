package commands.bot.economy;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.AccountNotFoundException;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;
import utilities.Utils;

public class EXP extends CustomCommand {
    public EXP() {
        this.name = "EXP";
        this.help = "Get your current EXP count";
        this.syntax = "!exp";
        this.category = MainBot.EXP;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.exp){
                commandEvent.reply("EXP is disabled on this server!");
                return;
            }
            double currentEXP = ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).exp;
            double nextEXP = Utils.getEXPForNextLevel(ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).level+1);
            commandEvent.reply("Current EXP: " + Utils.round(currentEXP) + "\tNext Level: " + Utils.round(nextEXP));
        }catch (ServerNotFoundException | AccountNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
