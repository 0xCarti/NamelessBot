package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.audio.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.FlagHandler;

import java.util.List;
import java.util.Locale;

public class Queue extends CustomCommand {
    public Queue() {
        this.name = "Queue";
        this.help = "Alter the music queue in some way.";
        this.syntax = "!queue [flag]";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
            return;
        }

        List<String> flags = FlagHandler.getFlags(commandEvent);
        if(!flags.isEmpty()){
            switch (flags.get(0).toLowerCase(Locale.ROOT)){
                case "-c" -> {
                    PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getQueue().clear();
                    commandEvent.reply("Music queue was cleared.");
                    return;
                }
                case "-s" -> {
                    PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.shuffle();
                    commandEvent.reply("Music queue was shuffled.");
                    return;
                }
                case "-h" -> commandEvent.reply(getDetailedHelp());
                default -> commandEvent.reply("Flag not recognized.");
            }
        }

        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(!args.isEmpty()){
            switch (args.get(0).toLowerCase(Locale.ROOT)){
                case "clear" -> {
                    PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getQueue().clear();
                    commandEvent.reply("Music queue was cleared.");
                    return;
                }
                default -> commandEvent.reply("Flag not recognized.");
            }
        }

        if(PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getQueue().isEmpty()){
            commandEvent.reply("The music queue is empty.");
            return;
        }

        String reply = "`" + PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).player.getPlayingTrack().getInfo().title + "`";
        if(reply.equalsIgnoreCase("unknown title")){
            reply = "`Local Audio File`";
        }

        int i = 1;
        for (AudioTrack track : PlayerManager.getInstance().getMusicManager(commandEvent.getGuild()).scheduler.getQueue()) {
            if(track.getInfo().title.equalsIgnoreCase("unknown title")){
                reply = reply + "\r\n" + i + ": Local Audio File";
            }else{
                reply = reply + "\r\n" + i + ": " + track.getInfo().title;
            }
            i++;
        }

        commandEvent.reply(reply);
    }
    public MessageEmbed getDetailedHelp(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(syntax);
        builder.setDescription(help);
        builder.addField("`-h`", "Displays this detailed help menu.", false);
        builder.addField("`-s`", "Shuffles the queue.", false);
        builder.addField("`-c`", "Clears the music queue.", false);
        builder.setFooter("For inquiries please contact: Carti#6852");
        return builder.build();
    }
}
