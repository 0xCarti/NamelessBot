package commands.bot.economy.games;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;

public class Roulette extends CustomCommand {
    public Roulette() {
        this.name = "Roulette";
        this.help = "Play a game of roulette!";
        this.syntax = "~roulette <red|green|black> <bet>";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        //Do code.
    }
}
