package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.audio.PlayerManager;
import main.managers.ServerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;

import java.util.concurrent.TimeUnit;

public class Player extends CustomCommand {
    public Player() {
        this.name = "Player";
        this.help = "Get a UI for the music player.";
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
            return;
        }

        if(PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getQueue().isEmpty()){
            commandEvent.reply("There is nothing playing.");
            return;
        }
        Message message = commandEvent.getChannel().sendMessageEmbeds(PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getQueueMessage()).setActionRow(
                new ButtonImpl("musicPrev", "Previous", ButtonStyle.PRIMARY, false, null),
                new ButtonImpl("musicPausePlay", "Play/Pause", ButtonStyle.PRIMARY, false, null),
                new ButtonImpl("musicNext", "Next", ButtonStyle.PRIMARY, false, null),
                new ButtonImpl("musicVolUp", "Volume Up", ButtonStyle.PRIMARY, false, null),
                new ButtonImpl("musicVolDown", "Volume Down", ButtonStyle.PRIMARY, false, null)
        ).complete();
        PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).playerMessages.add(message);
        message.delete().queueAfter(10, TimeUnit.MINUTES);
    }
}
