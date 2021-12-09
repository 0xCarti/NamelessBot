package commands.bot.economy.games;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ServerNotFoundException;
import main.MainBot;
import main.audio.PlayerManager;
import main.economy.RouletteWheel;
import main.managers.ServerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.FlagHandler;

import java.awt.*;
import java.util.List;
import java.util.Locale;

public class Roulette extends CustomCommand {
    public RouletteWheel wheel = new RouletteWheel();

    public Roulette() {
        this.name = "Roulette";
        this.help = "Play a game of roulette!";
        this.syntax = "~roulette <red|green|black> <bet>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy was not setup on this server.");
                return;
            }
        }catch (ServerNotFoundException e){
            commandEvent.reply(e.getMessage());
            return;
        }

        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.size() < 2){
            commandEvent.reply("Please specify a color and a bet!");
            return;
        }

        String color = args.get(0);

        double bet;
        try{
            bet = Double.parseDouble(args.get(1));
            if(bet <= 0){
                commandEvent.reply("Please specify a valid bet!");
                return;
            }
        }catch (NumberFormatException e){
            commandEvent.reply("Please specify a valid bet!");
            return;
        }

        try{
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.withdraw(commandEvent.getAuthor().getId(), bet);
        }catch (ServerNotFoundException | InsufficientFundsException | AccountNotFoundException e){
            commandEvent.reply(e.getMessage());
        }

        int spins = wheel.getSpins();
        Message message = commandEvent.getChannel().sendMessageEmbeds(displayWheel()).complete();
        for(int i = 0; i < 7 + spins; i++){
            wheel.spin();
            message.editMessageEmbeds(displayWheel()).complete();
            //message.editMessage(displayWheel()).complete();
        }

        String winner = wheel.getMiddle().replaceAll(":", "");
        if(winner.charAt(0) == color.charAt(0)){
            if(winner.charAt(0) == 'g'){
                bet *= 13;
            }else{
                bet *= 2;
            }

            try{
                ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.deposit(commandEvent.getAuthor().getId(), bet);
            }catch (ServerNotFoundException | AccountNotFoundException e){
                commandEvent.reply(e.getMessage());
            }
        }else{
            bet = 0;
        }

        commandEvent.reply(commandEvent.getMember().getEffectiveName() + " has spun and won $" + bet + "!");
        //wheel.reset();
    }

    private MessageEmbed displayWheel(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Roulette Wheel");
        builder.setDescription("The wheel is spinning...");
        builder.addField("", wheel.getLeft(), true);
        builder.addField(":arrow_down:", wheel.getMiddle(), true);
        builder.addField("", wheel.getRight(), true);
        switch (wheel.getMiddle().toLowerCase()){
            case ":red_circle:" -> builder.setColor(Color.RED);
            case ":green_circle:" -> builder.setColor(Color.GREEN);
            case ":black_circle:" -> builder.setColor(Color.BLACK);
        }
        return builder.build();
    }
}
