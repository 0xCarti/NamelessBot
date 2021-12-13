package commands.bot.economy;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.AccountNotFoundException;
import exceptions.ServerNotFoundException;
import main.MainBot;
import utilities.Utils;

public class Balance extends CustomCommand {
    public Balance() {
        this.name = "balance";
        this.help = "Checks the balance of you're account.";
        this.syntax = "!balance";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        try {
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy module is not enabled. Please use !enable econ to enable it.");
                return;
            }

            double balance = ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).balance;
            commandEvent.reply("$" + Utils.round(balance));
        } catch (ServerNotFoundException | AccountNotFoundException e) {
            commandEvent.reply(e.getMessage());
        }
    }
}
