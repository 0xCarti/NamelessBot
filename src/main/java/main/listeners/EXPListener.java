package main.listeners;

import com.jagrosh.jdautilities.command.CommandEvent;
import exceptions.AccountNotFoundException;
import exceptions.ServerNotFoundException;
import main.managers.ServerManager;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import utilities.Logger;

public class EXPListener implements EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof CommandEvent event){
            try{
                int level = ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).level;
                double exp = ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).generateEXP(genericEvent);
                ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).giveEXP(exp);
                if(level < ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).level){
                    int newLevel = ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).level;
                    event.reply("You have leveled up to level " + newLevel + "!");
                }
            }catch (ServerNotFoundException | AccountNotFoundException e){
                event.reply(e.getMessage());
            }
        }
        if (genericEvent instanceof MessageReceivedEvent event){
            if(event.getAuthor().isBot()){
                return;
            }

            try{
                int level = ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).level;
                double exp = ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).generateEXP(genericEvent);
                ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).giveEXP(exp);
                if(level < ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).level){
                    int newLevel = ServerManager.findServer(event.getGuild().getId()).accountManager.findAccount(event.getAuthor().getId()).level;
                    event.getMessage().reply("You have leveled up to level " + newLevel + "!");
                }
            }catch (ServerNotFoundException | AccountNotFoundException e){
                event.getMessage().reply(e.getMessage());
            }catch (InsufficientPermissionException e){
                Logger.debug(3, "Tried to reward EXP but user does not have permission to do so in [] of [].", event.getChannel().getName(), event.getGuild().getName());
            }catch (IllegalStateException ignored){}
        }
    }
}
