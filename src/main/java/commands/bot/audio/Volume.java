package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.OptionManager;
import main.managers.ServerManager;
import main.MainBot;
import main.audio.PlayerManager;
import utilities.FlagHandler;

import java.util.List;

public class Volume extends CustomCommand {
    public Volume() {
        this.name = "Volume";
        this.help = "Change the volume of the music.";
        this.syntax = "!volume <1-100>";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }

        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
            return;
        }

        if(!commandEvent.getGuild().getAudioManager().isConnected()){
            commandEvent.reply("The bot isn't playing audio.");
            return;
        }

        if(!commandEvent.getMember().getVoiceState().inVoiceChannel()){
            commandEvent.reply("Please be in a voice channel to change the volume of the song.");
            return;
        }

        int volume = Integer.parseInt(args.get(0));
        if(volume < 1 || volume > 100){
            commandEvent.reply("Please use a valid volume.");
            return;
        }

        PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.setVolume(volume);
        commandEvent.reply("Volume set to " + volume);
    }
}