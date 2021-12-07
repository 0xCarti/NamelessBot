package commands.bot.steam.csgo;

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

public class Stats extends CustomCommand {
    public Stats() {
        this.name = "Stats";
        this.help = "Get info on a CSGO profile.";
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
            commandEvent.reply(SteamManager.getCsgoUserStats(steamID));
        } catch (SteamApiException | MalformedURLException e) {
            commandEvent.reply("An error occurred while getting the stats. Make sure you are using a proper URL and the users privacy settings are public.");
            e.printStackTrace();
        }
    }
}
