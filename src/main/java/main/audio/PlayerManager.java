package main.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.*;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import utilities.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        musicManagers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(guild, this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                if(track.getInfo().title.equalsIgnoreCase("unknown title")){
                    channel.sendMessage("Adding to queue: `")
                            .append(trackUrl.substring(trackUrl.lastIndexOf("\\")+1) + "`")
                            .queue();
                }else{
                    channel.sendMessage("Adding to queue: ")
                            .append('`' + track.getInfo().title)
                            .append("` by `")
                            .append(track.getInfo().author)
                            .append('`')
                            .queue();
                }
            }
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if(playlist.isSearchResult()){
                    trackLoaded(playlist.getTracks().get(0));
                }else{
                    List<AudioTrack> tracks = playlist.getTracks();
                    for(int i = 0; i < 10; i++) {
                        AudioTrack track = playlist.getTracks().get(i);
                        musicManager.scheduler.queue(track);

                        channel.sendMessage("Adding to queue: ")
                                .append('`' + track.getInfo().title)
                                .append("` by `")
                                .append(track.getInfo().author)
                                .append('`')
                                .queue();
                    }
                }
            }
            @Override
            public void noMatches() {
                channel.sendMessage("No matches were found for: " + trackUrl.substring(trackUrl.lastIndexOf("\\")+1)).complete();
                channel.getGuild().getAudioManager().closeAudioConnection();
            }
            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
                channel.sendMessage("Failed to load audio from " + trackUrl.substring(trackUrl.lastIndexOf("\\")+1)).complete();
                Logger.debug(3, "Failed to load audio from [] for guild [].", trackUrl, channel.getGuild().getId());
                channel.getGuild().getAudioManager().closeAudioConnection();
            }
        });
    }

    public void loadNow(Guild guild, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);
        this.audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.player.startTrack(track, true);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {}

            @Override
            public void noMatches() {
                System.out.println("No matches were found for: " + trackUrl.substring(trackUrl.lastIndexOf("\\")+1));
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println("Failed to load audio: " + e.getMessage());
            }
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}