package commands.bot.economy.stock;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.InvalidTickerException;
import main.MainBot;
import main.managers.StockManager;
import utilities.FlagHandler;

import java.io.IOException;
import java.util.List;

public class Quote extends CustomCommand {
    public Quote() {
        this.name = "Quote";
        this.help = "Get info on a stock by the ticker.";
        this.syntax = "~Quote <ticker>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.size() < 1){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }

        String ticker = args.get(0);
        try {
            commandEvent.reply(StockManager.getQuoteEmbed(ticker));
        } catch (InvalidTickerException e) {
            commandEvent.reply(e.getMessage());
        } catch (IOException e) {
            commandEvent.reply("An error occurred while trying to get the quote.");
            e.printStackTrace();
        }
    }
}
