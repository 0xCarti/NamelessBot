package commands.bot.economy;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import main.MainBot;
import utilities.FlagHandler;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ServerNotFoundException;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class Transfer extends CustomCommand {
    public Transfer() {
        this.name = "Transfer";
        this.help = "Transfers money to another person's account.";
        this.syntax = "!transfer <mentionedUser> <amount>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.size() < 2){
            commandEvent.reply("Please use th proper syntax.");
            return;
        }

        Member receivingMember = commandEvent.getMessage().getMentionedMembers().get(0);

        if (receivingMember == null){
            commandEvent.reply("Please mention a user to transfer to.");
            return;
        }

        try {
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy is disabled on this server!");
                return;
            }

            double balance = Double.parseDouble(args.get(1));
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.transfer(commandEvent.getAuthor().getId(), args.get(0), balance);
            commandEvent.reply("Transfer was successfully completed.");
        } catch (ServerNotFoundException | AccountNotFoundException | InsufficientFundsException e) {
            commandEvent.reply(e.getMessage());
        }
    }
}
