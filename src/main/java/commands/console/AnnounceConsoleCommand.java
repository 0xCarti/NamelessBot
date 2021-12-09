package commands.console;

import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import utilities.Logger;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class AnnounceConsoleCommand extends ConsoleCommand {

    public AnnounceConsoleCommand() {
        this.name = "Announce";
        this.description = "Makes an announcement on all servers.";
        this.syntax = "/announce <message>";
    }

    @Override
    public void execute() {
        execute(new String[]{"This is a test message."});
    }

    @Override
    public void execute(String[] args) {
        for (Guild guild : MainBot.builder.getGuilds()) {
            try {
                guild.getDefaultChannel().sendMessageEmbeds(getAnnouncementEmbed(String.join(" ", args))).queue((message) ->{
                    message.delete().queueAfter(10, TimeUnit.MINUTES);
                });
            }catch (InsufficientPermissionException | ErrorResponseException e) {
                Logger.debug(3, "Announcement failed to send to " + guild.getName() + ".");
            }
        }
    }

    private MessageEmbed getAnnouncementEmbed(String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Announcement - Sesh's Shit Bot");
        builder.setDescription(message);
        builder.setColor(Color.RED);
        builder.addField("Support Server", "`https://discord.gg/wxKgrydA3z`", false);
        builder.setFooter("This message will delete in 10 minutes.");
        return builder.build();
    }
}