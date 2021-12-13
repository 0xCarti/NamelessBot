package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.audio.PlayerManager;

public class Next extends CustomCommand {
    public Next() {
        this.name = "Next";
        this.help = "Play the next song in the queue.\r\n\tNote: If the queue is empty then the bot will stop and leave.";
        this.syntax = "!next";
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
            return;
        }

        if(!commandEvent.getGuild().getAudioManager().isConnected()){
            commandEvent.reply("The bot isn't playing audio.");
            return;
        }

        if(!commandEvent.getMember().getVoiceState().inVoiceChannel()){
            commandEvent.reply("Please be in a voice channel to play the next song.");
            return;
        }

        PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.nextTrack();

        String trackName = PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.getPlayingTrack().getInfo().title;
        if(trackName.equalsIgnoreCase("unknown title")){
            trackName = "`Local Audio File`";
        }

        commandEvent.reply("Now playing: " + trackName);
    }
}

