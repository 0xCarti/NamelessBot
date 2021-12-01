package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import main.audio.TrackScheduler;
import main.managers.OptionManager;
import main.managers.ServerManager;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.FlagHandler;
import exceptions.ServerNotFoundException;
import main.audio.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import utilities.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class Play extends CustomCommand {
    public Play() {
        this.name = "Play";
        this.help = "Play a sound or song into the voice channel!";
        this.syntax = "!play <term|url> [flag]";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.getMessage().delete().queue();

        if(Record.recordingServers.contains(commandEvent.getGuild().getId())){
            commandEvent.reply("You can't play music while recording!");
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

        if(!commandEvent.getMember().getVoiceState().inVoiceChannel()){
            commandEvent.reply("Please be in a voice channel to play a sound.");
            return;
        }

        List<String> flags = FlagHandler.getFlags(commandEvent);
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty() && flags.isEmpty()){
            if(PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.getPlayingTrack() == null){
                commandEvent.reply("The queue is empty, nothing to play.");
                return;
            }else{
                PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.setPaused(!PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.isPaused());
            }
            return;
        }

        if(flags.isEmpty()){
            String argsString = FlagHandler.getArgsString(args, " ");
            if(FlagHandler.isUrl(argsString)){
                Logger.debug(1, "Loading audio from []", argsString);
                play(commandEvent, argsString);
            }else if(FlagHandler.isLocalUrl(argsString)){
                Logger.debug(1, "Loading local file from url []", argsString);
                argsString = Paths.get("").toAbsolutePath() + "\\clips\\" + commandEvent.getGuild().getId() + "\\" + argsString;
                play(commandEvent, argsString);
            }else if(FlagHandler.isSearchUrl(argsString)){
                Logger.debug(1, "Searching for audio from []", argsString);
                play(commandEvent, argsString);
            }else{
                commandEvent.reply("I'm having trouble parsing the arguments.");
            }
        }else{
            String flag = flags.remove(0);
            String searchTerm = FlagHandler.getArgsString(args, " ");

            switch (flag) {
                case "-y" -> play(commandEvent, "ytsearch: " + searchTerm);
                case "-s" -> play(commandEvent, "scsearch: " + searchTerm);
                case "-l" -> {
                    searchTerm = Paths.get("").toAbsolutePath() + "\\clips\\" + commandEvent.getGuild().getId() + "\\" + searchTerm;
                    if (new File(searchTerm + ".mp3").exists()) {
                        searchTerm = searchTerm + ".mp3";
                    } else if (new File(searchTerm + ".wav").exists()) {
                        searchTerm = searchTerm + ".wav";
                    } else {
                        searchTerm = searchTerm + ".ogg";
                    }
                    play(commandEvent, searchTerm);
                }
                case "-p" -> play(commandEvent, PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getPrevious().getIdentifier());
                case "-n" -> PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.nextTrack();
                case "-h" -> commandEvent.reply(getDetailedHelp());
                default -> commandEvent.reply("Could not parse flag properly.");
            }
        }
    }

    public void play(CommandEvent commandEvent, String url){
        TrackScheduler.annoying = false;
        TextChannel channel = commandEvent.getTextChannel();
        commandEvent.getGuild().getAudioManager().openAudioConnection(commandEvent.getMember().getVoiceState().getChannel());
        PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.setVolume(25);
        PlayerManager.getInstance().loadAndPlay(channel, url);
    }
    public MessageEmbed getDetailedHelp() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(syntax);
        builder.setDescription(help);
        builder.addField("`-y`", "Specifies a youtube search term.", false);
        builder.addField("`-s`", "Specifies a soundcloud search term.", false);
        builder.addField("`-l`", "Specifies a local search term.", false);
        builder.addField("`-p`", "Plays the previous track.", false);
        builder.addField("`-n`", "Plays the next track.", false);
        builder.addField("`-h`", "Shows this menu.", false);
        builder.setFooter("For inquiries please contact: Carti#6852");
        return builder.build();
    }
}
