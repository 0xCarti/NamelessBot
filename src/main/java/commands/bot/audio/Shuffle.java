package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.managers.OptionManager;
import main.managers.ServerManager;
import main.MainBot;
import main.audio.PlayerManager;

public class Shuffle extends CustomCommand {
    public Shuffle() {
        this.name = "Shuffle";
        this.help = "Shuffles the music queue.";
        this.syntax = "~shuffle";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio) {
                commandEvent.reply("This server does not have audio enabled.");
            }else{
                PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.shuffle();
                commandEvent.reply("Shuffled the queue!");
            }
        }catch (ServerNotFoundException e) {
            commandEvent.reply(e.getMessage());
        }
    }
}
