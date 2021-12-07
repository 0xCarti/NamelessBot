package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.audio.AudioRecordHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import utilities.FlagHandler;
import utilities.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Record extends CustomCommand {
    public static final HashSet<String> recordingServers = new HashSet<>();

    public Record() {
        this.name = "Record";
        this.help = "Start recording the voice channel for clipping.\r\n\tNote: If the bot is not recording then no need to specify a clip name.";
        this.syntax = "!record <clipName>";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }

        if(recordingServers.contains(commandEvent.getGuild().getId())){
            List<String> args = FlagHandler.getArgsList(commandEvent);
            if(args.isEmpty()){
                commandEvent.reply("Please use the proper syntax.");
                return;
            }

           try{
               clip(commandEvent, args.get(0));
           }catch (Exception e){
               e.printStackTrace();
           }
        }else{
            record(commandEvent);
        }
    }

    private void record(CommandEvent commandEvent){
        Member member = commandEvent.getMember();
        if(!member.getVoiceState().inVoiceChannel()){
            commandEvent.reply("Please be in a voice channel to start recording or clip.");
            return;
        }

        VoiceChannel channel = member.getVoiceState().getChannel();
        AudioManager audioManager = commandEvent.getGuild().getAudioManager();
        audioManager.setReceivingHandler(new AudioRecordHandler(commandEvent.getGuild()));
        audioManager.openAudioConnection(channel);
        recordingServers.add(commandEvent.getGuild().getId());
    }
    private void clip(CommandEvent commandEvent, String clipName){
        AudioManager audioManager = commandEvent.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String convert = "ffmpeg -f s16be -ar 44100 -ac 2 -i clip.pcm clip.mp3";
                String cut = "ffmpeg -sseof -10 -i clip.mp3 ./clips/" + commandEvent.getGuild().getId() + "/" + clipName + ".mp3";
                Logger.debug(1, "Running audio conversion and trimming task....");
                try {
                    Process proc = Runtime.getRuntime().exec(convert);
                    InputStream stderr = proc.getErrorStream();
                    InputStreamReader reader = new InputStreamReader(stderr);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    Logger.debug(1, "\tConverting new clip from PCM to MP3");
                    while (bufferedReader.readLine() != null);
                    if(proc.waitFor() > 0){
                        commandEvent.reply("Converting PCM to MP3 finished with an error.");
                        Logger.debug(3, "\t\tProcess exitValue: []", proc.waitFor());
                        return;
                    }
                    Logger.debug(1, "\t\tProcess exitValue: []",proc.waitFor());

                    proc = Runtime.getRuntime().exec(cut);
                    stderr = proc.getErrorStream();
                    reader = new InputStreamReader(stderr);
                    bufferedReader = new BufferedReader(reader);
                    Logger.debug(1, "\tCutting audio clip down to size.");
                    while (bufferedReader.readLine() != null);
                    if(proc.waitFor() > 0){
                        commandEvent.reply("Cutting audio finished with an error.");
                        Logger.debug(3, "\t\tProcess exitValue: []", proc.waitFor());
                        return;
                    }
                    Logger.debug(1, "\t\tProcess exitValue: []", proc.waitFor());

                    File file = new File("clip.pcm");
                    if (file.delete()) {
                        Logger.debug(1, "\tDeleted the file: []", file.getName());
                    } else {
                        Logger.debug(3, "\tFailed to delete the file []", file.getName());
                    }
                    file = new File("clip.mp3");
                    if (file.delete()) {
                        Logger.debug(1, "\tDeleted the file: []", file.getName());
                    } else {
                        Logger.debug(3, "\tFailed to delete the file []", file.getName());
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                Logger.debug(1, "Successfully rendered audio to [].mp3", clipName);
                commandEvent.getChannel().sendMessage(clipName + ".mp3 is now ready for use.").complete();
            }
        };
        timer.schedule(task, 2000);
        recordingServers.remove(commandEvent.getGuild().getId());
    }
}