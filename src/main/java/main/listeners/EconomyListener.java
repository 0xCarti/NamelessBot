package main.listeners;

import commands.bot.economy.games.Crash;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class EconomyListener implements EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if(genericEvent instanceof ButtonClickEvent event){
            if(event.getButton().getId().equalsIgnoreCase("btnStop")){
                if(Crash.runningMessages.contains(event.getMessageId())){
                    event.replyEmbeds(Crash.stop(event.getMessage(), event.getMember())).complete();
                }
            }
        }
    }
}
