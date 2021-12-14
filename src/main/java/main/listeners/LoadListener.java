package main.listeners;

import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.AccountManager;
import main.managers.ServerManager;
import main.managers.StockManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import utilities.Logger;

import java.io.FileNotFoundException;
import java.util.List;

public class LoadListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof ReadyEvent){
            List<Guild> servers = MainBot.builder.getGuilds();
            for (Guild server : servers) {
                Member owner = server.retrieveOwner().complete();
                Logger.debug(1, "Connected to: []\t[[]].", server.getName(), owner.getUser().getAsTag());
                try{
                    ServerManager.load(server.getId());
                    //remove the following
                    if(true){
                        ServerManager.findServer(server.getId()).stockManager = new StockManager();
                        for (AccountManager.Account account : ServerManager.findServer(server.getId()).accountManager.accounts) {
                            ServerManager.findServer(server.getId()).stockManager.addPortfolio(account.userID, account.name);
                        }
                    }
                }catch (FileNotFoundException | ServerNotFoundException e){
                    Logger.debug(3, "Save file not found for [], running setup now.", server.getName());
                    ServerManager.add(server);
                }
            }
            Logger.log("Finished connecting to all servers.");
        }
        if (genericEvent instanceof GuildJoinEvent){
            Guild server = ((GuildJoinEvent) genericEvent).getGuild();
            Member owner = server.retrieveOwner().complete();
            Logger.debug(1, "Connected to: []\t[[]]. Running setup now", server.getName(), owner.getUser().getAsTag());
            ServerManager.add(server);
        }
        if(genericEvent instanceof GuildMemberJoinEvent event){
            try{
                ServerManager.findServer(event.getGuild().getId()).accountManager.add(event.getMember().getId(), event.getMember().getEffectiveName());
                Logger.debug(2, "Added user [[]], to the account manager for [].", event.getMember().getEffectiveName(), event.getGuild().getName());
            }catch (ServerNotFoundException e){
                Logger.debug(3, e.getMessage());
            }
        }
    }
}
