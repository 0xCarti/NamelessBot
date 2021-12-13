package commands.bot.economy;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import commands.bot.economy.games.Game;
import main.MainBot;

import java.util.List;

public class Games extends CustomCommand {
    public Games() {
        this.name = "Games";
        this.help = "Get a list of games to play.";
        this.syntax = "!games";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        List<Command> commands = MainBot.cmdClient.getCommands();

        String reply = "";

        for (int i = 0; i < commands.size(); i++){
            if(commands.get(i) instanceof Game){
                reply = reply + "!" + commands.get(i).getName() + " - " + commands.get(i).getHelp() + "\r\n";
            }
        }

        commandEvent.reply(reply);
    }
}
