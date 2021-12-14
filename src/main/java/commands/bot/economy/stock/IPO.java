package commands.bot.economy.stock;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;

public class IPO extends CustomCommand {
    public IPO() {
        this.name = "IPO";
        this.help = "Get a list of upcoming IPO's";
        this.syntax = "~ipo <ticker>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        //Do code.
    }
}
