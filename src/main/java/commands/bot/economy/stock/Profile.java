package commands.bot.economy.stock;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.InvalidTickerException;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.ServerManager;
import main.managers.StockManager;
import utilities.FlagHandler;

import java.io.IOException;
import java.util.List;

public class Profile extends CustomCommand {
    public Profile() {
        this.name = "Profile";
        this.help = "Get a profile on a company by their ticker.";
        this.syntax = "~profile <ticker>";
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
            commandEvent.reply(StockManager.getCompanyEmbed(ticker));
        } catch (InvalidTickerException e) {
            commandEvent.reply(e.getMessage());
        } catch (IOException e) {
            commandEvent.reply("An error occurred while trying to get the quote.");
            e.printStackTrace();
        }
    }
}
