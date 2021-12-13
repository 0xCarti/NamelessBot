package main.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exceptions.ServerNotFoundException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import utilities.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {
    public static final List<Server> servers = new ArrayList<>();
    public final static Gson gson = new Gson();

    public static boolean add(Guild guild){
        Server server = new Server(guild.getId());
        server.setup(guild);
        boolean output = servers.add(server);
        save();
        return output;
    }
    public static void remove(String guildID) throws ServerNotFoundException {
        Server server = findServer(guildID);
        servers.remove(server);
        save();
    }

    public static Server findServer(String guildID) throws ServerNotFoundException {
        for (Server server : servers) {
            if(server.guildID.equals(guildID)){
                return server;
            }
        }
        throw new ServerNotFoundException("Server could not be found [" + guildID + "]. Try running !setup -h");
    }
    public static List<String> getServerIDs() {
        List<String> guildIDs = new ArrayList<>();
        for (Server server : servers) {
            guildIDs.add(server.guildID);
        }
        return guildIDs;
    }

    public static void save(){
        new File("./servers").mkdir();
        for (Server server : servers) {
            try{
                FileWriter writer = new FileWriter("./servers/" + server.guildID +".json");
                gson.toJson(server, server.getClass(), writer);
                writer.flush();
                writer.close();
            } catch (IOException e){
                Logger.debug(3, "Saving servers threw a IOException.");
            }
        }
    }
    public static void save(String guildID) throws ServerNotFoundException {
        Server server = findServer(guildID);
        try{
            FileWriter writer = new FileWriter("./servers/" + guildID +".json");
            gson.toJson(server, server.getClass(), writer);
            writer.flush();
            writer.close();
        } catch (IOException e){
            Logger.debug(3, "Saving server [] threw a IOException.", guildID);
        }
    }
    public static void load() throws FileNotFoundException {
        File file = new File("./servers");
        file.mkdir();
        FilenameFilter filter = (file1, name) -> name.endsWith(".json");

        String[] paths = file.list(filter);
        for (String path : paths) {
            Server server = gson.fromJson(
                    new FileReader("./servers/" + path),
                    new TypeToken<Server>(){}.getType());
            servers.add(server);
        }
    }
    public static void load(String guildID) throws FileNotFoundException {
        new File("./servers").mkdir();
        Server server = gson.fromJson(
                new FileReader("./servers/" + guildID + ".json"),
                new TypeToken<Server>(){}.getType());
        servers.add(server);
    }

    public static class Server{
        public String guildID;
        public AccountManager accountManager;
        public AudioManager audioManager;
        public OptionManager optionManager;

        public Server(String guildID){
            this.guildID = guildID;
            this.accountManager = new AccountManager();
            this.optionManager = new OptionManager();
        }

        public void setGuildID(String guildID) {
            this.guildID = guildID;
        }
        public void setAccountManager(AccountManager accountManager) {
            this.accountManager = accountManager;
        }
        public void setAudioManager(AudioManager audioManager) {
            this.audioManager = audioManager;
        }
        public void setOptionManager(OptionManager optionManager) {
            this.optionManager = optionManager;
        }

        public void setup(Guild guild){
            guild.loadMembers().onSuccess(members -> {
                Logger.debug(2, "Starting setup for [].", guild.getName());
                setupAccounts(members);
                setupAudio(guild);
                //Notify the people that everything loaded correctly.
                //guild.getDefaultChannel().sendMessage("Run !help admin to get started!").complete();
                Logger.debug(2, "Setup is complete for [].", guild.getName());
            });
        }

        //Economy Setup
        private void setupAccounts(List<Member> members){
            Logger.debug(1, "\tInitializing Account Setup...");
            for (Member member : members) {
                if(!member.getUser().isBot()){
                    accountManager.add(member.getId(), member.getEffectiveName(), 5000.0, 0.0, 1);
                    Logger.debug(1,"\t\tSetup account for []", member.getEffectiveName());
                }
            }
        }
        //Setup Audio
        private void setupAudio(Guild guild){
            new File("./clips/" + guild.getId()).mkdirs();
            Logger.debug(1, "\tAudio setup successfully.");
        }

        //Developmental Setups

        public String toString() {
            return "Server{" +
                    "guildID='" + guildID + '\'' +
                    ", accountManager=" + accountManager +
                    ", Audio Manager=" + audioManager +
                    '}';
        }
    }
}
