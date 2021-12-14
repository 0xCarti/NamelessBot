package commands.bot.economy.stock;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.ServerManager;

public class Portfolio extends CustomCommand {
    public Portfolio() {
        this.name = "Portfolio";
        this.help = "Get info on your current stocks.";
        this.syntax = "~portfolio";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy module is not enabled. Please use !enable econ to enable it.");
                return;
            }

            if(ServerManager.findServer(commandEvent.getGuild().getId()).stockManager.getPortfolio(commandEvent.getAuthor().getId()).stocks.isEmpty()){
                commandEvent.reply("You don't have any stocks.");
                return;
            }

            commandEvent.reply(ServerManager.findServer(commandEvent.getGuild().getId()).stockManager.getPortfolio(commandEvent.getAuthor().getId()).getPortfolioEmbed());
        } catch (ServerNotFoundException e) {
            commandEvent.reply(e.getMessage());
        }
    }
}
