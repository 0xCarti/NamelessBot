package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.CustomCommand;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.OptionManager;
import main.managers.ServerManager;
import utilities.Logger;

import java.io.File;
import java.nio.file.Paths;

public class Upload extends CustomCommand {
    public Upload() {
        this.name = "Upload";
        this.help = "Upload a audio file to the bot.";
        this.syntax = "!upload (attach a audio file)";
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

        if(commandEvent.getMessage().getAttachments().size() < 1){
            commandEvent.reply("You must attach a audio file.");
            return;
        }

        String path = Paths.get("").toAbsolutePath() + "\\clips\\" + commandEvent.getGuild().getId() + "\\";
        commandEvent.getMessage().getAttachments().get(0).downloadToFile().thenAccept(file -> {
            if(file.renameTo(new File(path + file.getName()))){
                commandEvent.reply("File uploaded.");
                Logger.debug(1, "File uploaded to " + file.getAbsolutePath());
            }else{
                Logger.debug(3, "Rename Failed.");
            }
        }).exceptionally(err -> {
            Logger.debug(3, "Upload Failed.");
            err.printStackTrace();
            return null;
        });
    }
}
