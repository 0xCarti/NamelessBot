package commands.bot.economy.games;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.economy.SlotMachine;
import utilities.Utils;

public class Slots extends CustomCommand implements Game{
    private final SlotMachine slotMachine = new SlotMachine();

    public Slots() {
        this.name = "Slots";
        this.help = "Throw away your kids college fund on a few rounds of slaps... we'll only judge you a bit Quinton.";
        this.syntax = "!slots <bet>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        String[] args = Utils.getArgs(commandEvent);
        if(args.length < 1){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }

        commandEvent.getMessage().delete().complete();

        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy is disabled on this server!");
                return;
            }

            double bet = Double.parseDouble(args[0]);
            if(bet < 100){
                commandEvent.reply("Minimum bet is $100.");
                return;
            }

            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).withdraw(bet);
            slotMachine.display(commandEvent);
            double result = slotMachine.spin() * bet;
            slotMachine.reset();
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).deposit(result);
            commandEvent.reply(commandEvent.getAuthor().getName() + " Won $" + result + ".");
        }catch (ServerNotFoundException | AccountNotFoundException | InsufficientFundsException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
