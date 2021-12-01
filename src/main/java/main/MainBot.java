package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import commands.bot.admin.*;
import commands.bot.ai.Chat;
import commands.bot.ai.Question;
import commands.bot.audio.Record;
import commands.bot.economy.*;
import commands.bot.audio.*;
import commands.bot.economy.games.Crash;
import commands.bot.economy.games.Dice;
import commands.bot.economy.games.Slots;
import commands.bot.misc.*;
import commands.bot.audio.Previous;
import commands.bot.admin.Disable;
import commands.bot.admin.Enable;
import commands.bot.audio.Delete;
import commands.bot.audio.Upload;
import commands.console.AnnounceConsoleCommand;
import commands.console.SaveConsoleCommand;
import main.listeners.EXPListener;
import main.listeners.EconomyListener;
import main.managers.ConsoleCommandManager;
import utilities.ConsoleCommandThread;
import commands.console.StopConsoleCommand;
import main.managers.ServerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import utilities.Config;
import main.listeners.LoadListener;
import utilities.Logger;
import utilities.Utils;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class MainBot {
    public static JDA builder;
    public static CommandClientBuilder cmdBuilder;
    public static CommandClient cmdClient;
    public static final Command.Category ECONOMY = new Command.Category("Economy");
    public static final Command.Category EXP = new Command.Category("Exp");
    public static final Command.Category AUDIO = new Command.Category("Audio");
    public static final Command.Category MISC = new Command.Category("Misc");
    public static final Command.Category AI = new Command.Category("AI");
    public static final Command.Category ADMIN = new Command.Category("Admin");
    public static final Config config = new Config();

    public static void main(String[] args) throws LoginException, IOException {
        Utils.enableConsoleColour();

        //Load Servers
        ServerManager.load();
        Logger.debug("Bot is setup on [] servers.", ServerManager.servers.size());

        //Create a command builder and add commands to it.
        Logger.debug("Loading commands into command builder...");
        cmdBuilder = new CommandClientBuilder()
                .setPrefix(Config.PREFIX)
                .setOwnerId(Config.OWNER_ID)
                .setHelpWord("HelpDM")
                .setActivity(Activity.listening(Config.PREFIX + "help"))
                //Admin Commands
                .addCommand(new Enable())
                .addCommand(new Disable())
                .addCommand(new Disconnect())
                .addCommand(new Stimulate())
                //Economy Commands
                .addCommand(new Balance())
                .addCommand(new Transfer())
                .addCommand(new Dice())
                .addCommand(new Slots())
                .addCommand(new Crash())
                .addCommand(new Games())
                //EXP Commands
                .addCommand(new EXP())
                .addCommand(new Level())
                .addCommand(new Convert())
                //Music Commands
                .addCommand(new Record())
                .addCommand(new Play())
                .addCommand(new Pause())
                .addCommand(new Stop())
                .addCommand(new List())
                .addCommand(new Queue())
                .addCommand(new Next())
                .addCommand(new Previous())
                .addCommand(new Volume())
                .addCommand(new Shuffle())
                .addCommand(new Delete())
                .addCommand(new Upload())
                .addCommand(new Annoy())
                //Misc Commands
                .addCommand(new Ping())
                .addCommand(new Invite())
                .addCommand(new Changelog())
                //.addCommand(new Version())
                .addCommand(new Help())
                //AI Commands
                .addCommand(new Question())
                .addCommand(new Chat());
        ConsoleCommandManager.registerCommand(new StopConsoleCommand());
        ConsoleCommandManager.registerCommand(new SaveConsoleCommand());
        ConsoleCommandManager.registerCommand(new AnnounceConsoleCommand());

        cmdClient = cmdBuilder.build(); //Create command client by building the builder.
        Logger.debug("Loaded [] commands successfully.", cmdClient.getCommands().size());

        //Create the JDA builder with bot token.
        if(args.length != 0 && args[0].contains("t") && !Config.TEST_TOKEN.equals("")){
            builder = JDABuilder
                    .createDefault(Config.TEST_TOKEN)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
            Logger.debug("JDA Client was built with ID: []", Config.TEST_TOKEN);
        }else{
            builder = JDABuilder
                    .createDefault(Config.LIVE_TOKEN)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
            Logger.debug("JDA Client was built with ID: []", Config.LIVE_TOKEN);
        }

        //Add the command client as a listener
        builder.addEventListener(
                cmdClient,
                new LoadListener(),
                new EconomyListener(),
                new EXPListener());
        Logger.debug("Event listeners have been registered.");

        new File("./clips").mkdirs();
        ConsoleCommandThread thread = new ConsoleCommandThread();
        thread.start();
    }
}
