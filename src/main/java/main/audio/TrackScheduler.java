package main.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import utilities.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    public final Guild guild;
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private AudioTrack previous;
    public static boolean annoying = false; //FIX THIS

    public TrackScheduler(Guild guild, AudioPlayer player) {
        this.guild = guild;
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }
    public void queue(AudioTrack track){
        if(!this.player.startTrack(track, true)){
            this.queue.offer(track);
        }
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            if(!queue.isEmpty()){
                nextTrack();
            }else if (!annoying){
                guild.getAudioManager().closeAudioConnection();
            }
        }
    }

    public void nextTrack(){
        previous = player.getPlayingTrack();
        if(!this.player.startTrack(this.queue.poll(), false)){
            guild.getAudioManager().closeAudioConnection();
        }
    }
    public void prevTrack(){
        if(!this.player.startTrack(previous, true)){
            guild.getAudioManager().closeAudioConnection();
        }
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }
    public AudioTrack getPrevious(){
        return previous;
    }

    public void shuffle(){
        List<AudioTrack> tmpList = queue.stream().toList();
        tmpList = new ArrayList<>(tmpList);
        Collections.shuffle(tmpList);
        queue.clear();
        for (AudioTrack track : tmpList){
            queue.offer(track);
        }
    }
}
