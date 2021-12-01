package commands.bot.economy.games;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import main.managers.OptionManager;
import main.managers.ServerManager;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ServerNotFoundException;
import main.MainBot;
import utilities.Utils;

public class Dice extends CustomCommand implements Game {
    public Dice() {
        this.name = "Dice";
        this.help = "Bet money on a classic game of dice! Two Dice are rolled.";
        this.syntax = "!dice <dice-sum> <bet>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] args = Utils.getArgs(commandEvent);
        if(args.length < 2){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }

        int sum = Integer.parseInt(args[0]);

        if (sum < 2 || sum > 12){
            commandEvent.reply("Please enter a sum between 1 and 12.");
            return;
        }

        double bet = Double.parseDouble(args[1]);

        try {
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy is disabled on this server!");
                return;
            }

            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).withdraw(bet);
            int result = Utils.getRandomInt(12);

            if(sum == result){
                ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).deposit(bet*result);
                commandEvent.reply("Roll = " + result + ". " + commandEvent.getMember().getEffectiveName() + " won $" + bet*result);
            }else{
                commandEvent.reply("Roll = " + result + ". Better luck next time pal.");
            }
        } catch (ServerNotFoundException | AccountNotFoundException | InsufficientFundsException e) {
            commandEvent.reply(e.getMessage());
        }
    }
}
