package main.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

public class GuildMusicManager {
    public final Guild guild;
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    private final AudioSendHandler sendHandler;

    public GuildMusicManager(Guild guild, AudioPlayerManager manager) {
        this.guild = guild;
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(guild, this.player);
        this.player.addListener(this.scheduler);
        this.sendHandler = new AudioPlayHandler(this.player);
    }

    public AudioSendHandler getSendHandler() {
        return sendHandler;
    }
}