package commands.bot.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.managers.ServerManager;
import exceptions.ServerNotFoundException;
import main.MainBot;

import java.io.File;
import java.nio.file.Paths;

public class List extends CustomCommand {
    public List() {
        this.name = "List";
        this.help = "Lists all the local audio files you can play.\r\n\tSyntax: !list";
        this.syntax = "!list";
        this.category = MainBot.AUDIO;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.audio){
                commandEvent.reply("This server does not have audio enabled.");
                return;
            }

            String reply = "";

            File file = new File(Paths.get("").toAbsolutePath() + "\\clips\\" + commandEvent.getGuild().getId());
            if(file.exists()){
                String[] paths = file.list();
                if(paths == null){
                    reply = "No files found.";
                }else{
                    for (String path : paths) {
                        reply = reply + "\r\n" + path;
                    }
                }
            }

            commandEvent.reply(reply);
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
        }
    }
}
