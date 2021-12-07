package commands.bot.steam;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.ServerManager;
import main.managers.SteamManager;
import org.steambuff.exception.SteamApiException;
import utilities.Config;
import utilities.FlagHandler;

import java.net.MalformedURLException;
import java.util.List;

public class Steam extends CustomCommand {
    public Steam() {
        this.name = "Steam";
        this.help = "Get info on a steam user.";
        this.syntax = Config.PREFIX + this.name + " <steamID|URL>";
        this.category = MainBot.STEAM;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.steam){
                commandEvent.reply("This server does not have steam enabled.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
            return;
        }

        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("You must provide a steamID or URL.");
            return;
        }

        String steamID = args.get(0);
        try {
            commandEvent.reply(SteamManager.getUserStats(steamID));
        } catch (SteamApiException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
