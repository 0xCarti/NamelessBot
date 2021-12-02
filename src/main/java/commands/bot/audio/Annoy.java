package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.audio.AudioRecordHandler;
import main.audio.TrackScheduler;
import main.managers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.FlagHandler;

import java.util.HashSet;
import java.util.List;

public class Annoy extends CustomCommand {
    public static HashSet<String> members = new HashSet<>();

    public Annoy() {
        this.name = "Annoy";
        this.help = "Annoy someone who's being annoying in the voice channel.";
        this.syntax = "~annoy <mention-user> [flags]";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("Audio was not setup on this server.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
            return;
        }

        if(!commandEvent.getMember().getVoiceState().inVoiceChannel()){
            commandEvent.reply("Please be in a voice channel to annoy someone.");
            return;
        }

        if(commandEvent.getMessage().getMentionedMembers().isEmpty()){
            commandEvent.reply("Please mention someone to annoy.");
            return;
        }

        if(members.contains(commandEvent.getMember().getId())){
            commandEvent.reply("You can't annoy someone while being annoyed.");
            return;
        }

        Member member = commandEvent.getMessage().getMentionedMembers().get(0);
        if(commandEvent.getMember().getId().equals(member.getId())){
            commandEvent.reply("You can't annoy yourself.");
            return;
        }

        List<String> flags = FlagHandler.getFlags(commandEvent);
        if(!flags.isEmpty()){
            switch (flags.get(0).toLowerCase()){
                case "-s" -> {
                    members.remove(member.getUser().getId());
                    commandEvent.getGuild().getAudioManager().closeAudioConnection();
                    TrackScheduler.annoying = false;
                }
                case "-h" -> commandEvent.reply(getDetailedHelp());
                default -> commandEvent.reply("I could not parse the flag.");
            }
            return;
        }

        members.add(member.getUser().getId());
        commandEvent.getGuild().getAudioManager().setReceivingHandler(new AudioRecordHandler(commandEvent.getGuild()));
        commandEvent.getGuild().getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
    }

    public MessageEmbed getDetailedHelp(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(syntax);
        builder.setDescription(help);
        builder.addField("`-s`", "Stop annoying the user.", false);
        builder.addField("`-h`", "Get this help menu.", false);
        builder.setFooter("For inquiries please contact: Carti#6852");
        return builder.build();
    }
}
