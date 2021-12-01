package commands.bot.economy;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import main.managers.ServerManager;
import main.MainBot;
import utilities.FlagHandler;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ServerNotFoundException;
import utilities.Utils;

import java.util.List;

public class Convert extends CustomCommand {
    public Convert() {
        this.name = "Convert";
        this.help = "Convert money to EXP";
        this.syntax = "!convert <spendAmount>";
        this.category = MainBot.EXP;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.size() < 1){
            commandEvent.reply("Please use th proper syntax.");
            return;
        }

        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.exp){
                commandEvent.reply("EXP is disabled on this server!");
                return;
            }

            double amount = Double.parseDouble(args.get(0));
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).withdraw(amount);
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).giveEXP(amount);
            commandEvent.reply("You now have " + Utils.round(ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).exp));
        }catch (ServerNotFoundException | AccountNotFoundException | InsufficientFundsException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
