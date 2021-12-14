package commands.console;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.managers.ServerManager;
import net.dv8tion.jda.api.entities.Guild;
import utilities.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;

public class CleanConsoleCommand extends ConsoleCommand {
    public static Gson gson = new Gson();
    public CleanConsoleCommand() {
        this.name = "Clean";
        this.description = "Cleans server files.";
        this.syntax = "/clean";
    }

    @Override
    public void execute(){
        Logger.debug(2, "Cleaning servers...");
        File file = new File("./servers");
        file.mkdir();
        FilenameFilter filter = (file1, name) -> name.endsWith(".json");

        String[] paths = file.list(filter);
        for (String path : paths) {
            String ID = path.replace(".json", "");
            boolean found = false;
            for (Guild guild : MainBot.builder.getGuilds()) {
                if(guild.getId().equals(ID)){
                    found = true;
                }
            }
            if(!found){
                File toDelete = new File("./servers/" + ID + ".json");
                try{
                    ServerManager.remove(ID);
                }catch (ServerNotFoundException e){
                    Logger.error(e.getMessage());
                }
                if(toDelete.delete()){
                    Logger.debug(2, "Deleted server: " + ID);
                }else{
                    Logger.debug(2, "Failed to delete server: " + ID);
                }
            }
        }
        Logger.debug(2, "Finished cleaning servers.");
    }

    @Override
    public void execute(String[] args) {
        execute();
    }
}
