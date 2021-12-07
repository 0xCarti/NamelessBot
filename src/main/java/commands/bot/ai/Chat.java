package commands.bot.ai;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import utilities.GPT3Impl;
import main.MainBot;
import utilities.FlagHandler;
import utilities.Logger;

import java.util.List;

public class Chat extends CustomCommand {
    public Chat() {
        this.name = "Chat";
        this.help = "Start chatting with the bot.";
        this.category = MainBot.AI;
        this.syntax = "!chat <message>";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if (args.size() == 0) {
            commandEvent.reply("Please provide a message to send.");
            Logger.debug(3, "Testing this shit");
            return;
        }

        String message = String.join(" ", args);
        commandEvent.reply(GPT3Impl.getFriendResponse(message));
    }
}
