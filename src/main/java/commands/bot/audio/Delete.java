package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.OptionManager;
import main.managers.ServerManager;
import utilities.FlagHandler;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class Delete extends CustomCommand {
    public Delete() {
        this.name = "Delete";
        this.help = "Delete a specified audio file from the bot.";
        this.syntax = "!delete <fileName>";
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
        }

        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("Please use the proper syntax.");
            return;
        }

        String fileName = args.get(0);
        fileName = Paths.get("").toAbsolutePath() + "\\clips\\" + commandEvent.getGuild().getId() + "\\" + fileName;
        if (new File(fileName + ".mp3").exists()) {
            fileName = fileName + ".mp3";
        } else if (new File(fileName + ".wav").exists()) {
            fileName = fileName + ".wav";
        } else if(new File(fileName + ".ogg").exists()){
            fileName = fileName + ".ogg";
        }

        //delete file
        if (new File(fileName).delete()) {
            commandEvent.reply("Deleted file.");
        }else{
            commandEvent.reply("Could not delete file.");
        }
    }
}
