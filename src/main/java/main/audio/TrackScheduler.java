package main.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.Utils;

import java.awt.*;
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
    public boolean annoying = false; //FIX THIS

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
        previous = player.getPlayingTrack().makeClone();
        if(!this.player.startTrack(this.queue.poll(), false)){
            guild.getAudioManager().closeAudioConnection();
        }
    }
    public void prevTrack(){
        AudioTrack tmp = player.getPlayingTrack().makeClone();
        if(!this.player.startTrack(previous, false)){
            guild.getAudioManager().closeAudioConnection();
        }
        previous = tmp;
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

    public MessageEmbed getQueueMessage(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Queue - " + queue.size() + " Songs");
        builder.setColor(Color.MAGENTA);
        if(player.isPaused()){
            builder.setDescription("**Paused**");
        }else{
            builder.setDescription("**Playing**");
        }
        try{
            builder.addField("Previous", "`" + previous.getInfo().title + "`", true);
        }catch (NullPointerException e){
            builder.addField("Previous", "`Nothing Played Previously`", true);
        }
        builder.addField("Current", "`" + player.getPlayingTrack().getInfo().title + "`", true);
        try{
            builder.addField("Next", "`" + queue.peek().getInfo().title + "`", true);
        }catch (NullPointerException e){
            builder.addField("Next", "`Nothing Queued Next`", true);
        }
        builder.setFooter("Volume: " + player.getVolume() + "%", null);
        return builder.build();
    }
}
