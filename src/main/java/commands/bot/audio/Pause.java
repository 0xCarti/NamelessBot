package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.OptionManager;
import main.managers.ServerManager;
import main.MainBot;
import main.audio.PlayerManager;

public class Pause extends CustomCommand {
    public Pause() {
        this.name = "Pause";
        this.help = "Pause the music.\r\n\tSyntax: !pause";
        this.syntax = "!pause";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }

            PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.setPaused(!PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.isPaused());
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
