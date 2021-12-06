package main.listeners;

import main.audio.PlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class MusicListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if(genericEvent instanceof ButtonClickEvent event){
            Message message = event.getMessage();
            if(PlayerManager.getInstance().getMusicManager(message.getGuild()).playerMessages.contains(message)){
                try {
                    switch (event.getButton().getId()) {
                        case "musicPrev" -> {
                            PlayerManager.getInstance().getMusicManager(message.getGuild()).scheduler.prevTrack();
                            event.getMessage().editMessageEmbeds(PlayerManager.getInstance().getMusicManager(message.getGuild()).scheduler.getQueueMessage()).queue();
                            event.reply("").queue(success -> success.retrieveOriginal().queue(reply -> reply.delete().queue()));
                        }
                        case "musicPausePlay" -> {
                            PlayerManager.getInstance().getMusicManager(message.getGuild()).player.setPaused(!PlayerManager.getInstance().getMusicManager(message.getGuild()).player.isPaused());
                            event.reply("").queue(success -> success.retrieveOriginal().queue(reply -> reply.delete().queue()));
                        }
                        case "musicNext" -> {
                            PlayerManager.getInstance().getMusicManager(message.getGuild()).scheduler.nextTrack();
                            event.getMessage().editMessageEmbeds(PlayerManager.getInstance().getMusicManager(message.getGuild()).scheduler.getQueueMessage()).queue();
                            event.reply("").queue(success -> success.retrieveOriginal().queue(reply -> reply.delete().queue()));
                        }
                        case "musicVolUp" -> {
                            PlayerManager.getInstance().getMusicManager(message.getGuild()).player.setVolume(PlayerManager.getInstance().getMusicManager(message.getGuild()).player.getVolume() + 10);
                            event.getMessage().editMessageEmbeds(PlayerManager.getInstance().getMusicManager(message.getGuild()).scheduler.getQueueMessage()).queue();
                            event.reply("").queue(success -> success.retrieveOriginal().queue(reply -> reply.delete().queue()));
                        }
                        case "musicVolDown" -> {
                            PlayerManager.getInstance().getMusicManager(message.getGuild()).player.setVolume(PlayerManager.getInstance().getMusicManager(message.getGuild()).player.getVolume() - 10);
                            event.getMessage().editMessageEmbeds(PlayerManager.getInstance().getMusicManager(message.getGuild()).scheduler.getQueueMessage()).queue();
                            event.reply("").queue(success -> success.retrieveOriginal().queue(reply -> reply.delete().queue()));
                        }
                    }
                }catch (NullPointerException e){
                    System.out.println("NullPointerException in MusicListener");
                    event.getGuild().getAudioManager().closeAudioConnection();
                    event.reply("Something went wrong.").queue();
                }
            }
        }
    }
}
