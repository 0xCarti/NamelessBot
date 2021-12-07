package commands.bot.ai;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import utilities.GPT3Impl;
import main.MainBot;
import utilities.FlagHandler;

import java.util.List;

public class Question extends CustomCommand {
    public Question() {
        this.name = "Question";
        this.help = "Get the answer to a generic question.";
        this.category = MainBot.AI;
        this.syntax = "!question <question to ask>";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if (args.size() == 0) {
            commandEvent.reply("Please provide a question to ask.");
            return;
        }

        String question = String.join(" ", args);
        commandEvent.reply(GPT3Impl.getGenericAnswer(question));
    }
}
