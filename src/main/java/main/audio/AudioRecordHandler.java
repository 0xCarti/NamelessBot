package main.audio;

import commands.bot.audio.Annoy;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecordHandler implements AudioReceiveHandler {
    public Guild guild;

    public AudioRecordHandler(Guild guild) {
        this.guild = guild;
    }

    @Override
    public boolean canReceiveUser() {
        return true;
    }

    @Override
    public void handleUserAudio(@NotNull UserAudio userAudio) {
        if(Annoy.members.contains(userAudio.getUser().getId())) {
            PlayerManager.getInstance().loadNow(guild, "https://soundcloud.com/yungsesh/annoy?si=34f265eddd7b4390b8a05ecf1f7b584f");
            TrackScheduler.annoying = true;
        }
    }

    //----Audio Receive Handling----//
    @Override
    public boolean canReceiveCombined() {
        return true;
    }

    @Override
    public void handleCombinedAudio(CombinedAudio combinedAudio) {
        try {
            File outputFile = new File("clip.pcm");
            outputFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(outputFile, true);
            outputStream.write(combinedAudio.getAudioData(1.0f));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
