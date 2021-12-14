package commands.bot.economy.stock;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidTickerException;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.ServerManager;
import main.managers.StockManager;
import utilities.FlagHandler;
import utilities.Logger;

import java.io.IOException;
import java.util.List;

public class Stock extends CustomCommand {
    public Stock() {
        this.name = "Stock";
        this.help = "Buy or sell a stock from a company.";
        this.syntax = "~stock <buy|sell> <ticker> <amount>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy module is not enabled. Please use !enable econ to enable it.");
                return;
            }


        } catch (ServerNotFoundException e) {
            commandEvent.reply(e.getMessage());
        }

        super.execute(commandEvent);
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.size() < 3){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }

        String action = args.get(0);
        try{
            switch (action.toLowerCase().charAt(0)) {
                case 'b' -> buy(commandEvent, args);
                case 's' -> sell(commandEvent, args);
            }
        }catch (InvalidTickerException | ServerNotFoundException | AccountNotFoundException | InsufficientFundsException e){
            commandEvent.reply(e.getMessage());
        }catch (IOException e){
            Logger.error(e.getMessage());
        }
    }

    private void buy(CommandEvent commandEvent, List<String> args) throws InvalidTickerException, IOException, ServerNotFoundException, AccountNotFoundException, InsufficientFundsException {
        String ticker = args.get(1);
        int amount = Integer.parseInt(args.get(2));
        double price = StockManager.getStockPrice(ticker)*amount;
        ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).withdraw(price);
        ServerManager.findServer(commandEvent.getGuild().getId()).stockManager.getPortfolio(commandEvent.getAuthor().getId()).addStock(ticker, amount);
        commandEvent.reply("Bought " + amount + " shares of " + ticker.toUpperCase() + " for $" + price + ".");
    }

    private void sell(CommandEvent commandEvent, List<String> args) throws InvalidTickerException, IOException, ServerNotFoundException, AccountNotFoundException {
        String ticker = args.get(1);
        int amount = Integer.parseInt(args.get(2));
        StockManager.Stock stock = ServerManager.findServer(commandEvent.getGuild().getId()).stockManager.getPortfolio(commandEvent.getAuthor().getId()).findStock(ticker);
        if(stock == null){
            commandEvent.reply("You don't own that stock.");
            return;
        }
        double gained = ServerManager.findServer(commandEvent.getGuild().getId()).stockManager.getPortfolio(commandEvent.getAuthor().getId()).removeStock(ticker, amount);
        ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.findAccount(commandEvent.getAuthor().getId()).deposit(gained);
        commandEvent.reply("Sold $" + gained + " worth of " + ticker.toUpperCase() + "shares.");
    }
}
