package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.OptionManager;
import main.managers.ServerManager;
import main.MainBot;
import main.audio.PlayerManager;

public class Previous extends CustomCommand {
    public Previous() {
        this.name = "Previous";
        this.help = "Plays the previous song.";
        this.syntax = "!previous";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }
        } catch (ServerNotFoundException e) {
            commandEvent.reply(e.getMessage());
        }

        if(!commandEvent.getSelfMember().getVoiceState().inVoiceChannel()){
            commandEvent.reply("I'm not connected to a channel right now.");
            return;
        }

        if(!commandEvent.getGuild().getAudioManager().isConnected()){
            commandEvent.reply("I'm not playing audio right now.");
            return;
        }

        PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.prevTrack();
        String trackName = PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.getPlayingTrack().getInfo().title;
        if(trackName.equalsIgnoreCase("unknown title")){
            trackName = "`Local Audio File`";
        }

        commandEvent.reply("Now playing: " + trackName);
    }
}
