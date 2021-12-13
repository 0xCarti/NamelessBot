package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.ServerManager;
import main.MainBot;
import main.audio.PlayerManager;

public class Stop extends CustomCommand {
    public Stop() {
        this.name = "Stop";
        this.help = "Stop music from playing.";
        this.syntax = "!stop";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
        if(commandEvent.getGuild().getAudioManager().isConnected()){
            commandEvent.getGuild().getAudioManager().closeAudioConnection();
            PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.stopTrack();
            commandEvent.reply("Stopped playing audio.");
        }else{
            commandEvent.reply("Bot wasn't playing audio.");
        }
    }
}
