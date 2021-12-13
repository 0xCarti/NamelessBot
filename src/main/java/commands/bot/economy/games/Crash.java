package commands.bot.economy.games;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ServerNotFoundException;
import main.managers.ServerManager;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;
import utilities.FlagHandler;
import utilities.Logger;
import utilities.Utils;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Crash extends CustomCommand implements Game {
    public static final HashSet<String> runningMessages = new HashSet<>();
    public static Timer timer = new Timer();

    public Crash() {
        this.name = "Crash";
        this.help = "Start an increasing multiplier and stop it before it crashes.";
        this.syntax = "!crash <bet>";
        this.category = MainBot.ECONOMY;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        super.execute(commandEvent);
        List<String> args = FlagHandler.getArgsList(commandEvent);
        if(args.isEmpty()){
            commandEvent.reply("You need to specify a bet!");
            return;
        }

        try{
            if(!ServerManager.findServer(commandEvent.getGuild().getId()).optionManager.economy){
                commandEvent.reply("Economy is disabled on this server!");
                return;
            }

            double bet = Double.parseDouble(args.get(0));
            ServerManager.findServer(commandEvent.getGuild().getId()).accountManager.withdraw(commandEvent.getAuthor().getId(), bet);
            final double[] multiplier = {1};
            Message message  = commandEvent.getChannel().sendMessageEmbeds(getEmbed(commandEvent.getMember().getEffectiveName(), multiplier[0], bet)).setActionRow(
                    new ButtonImpl("btnStop", "Stop", ButtonStyle.PRIMARY, false, null)
            ).complete();
            runningMessages.add(message.getId());
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                   if(Utils.getRandomInt(100)%10 == 0){
                       runningMessages.remove(message.getId());
                       TextChannel channel = message.getTextChannel();
                       channel.sendMessageEmbeds(crash(message.getMember().getEffectiveName(), multiplier[0], bet)).complete().delete().queueAfter(1, TimeUnit.MINUTES);
                       message.delete().complete();
                       timer.cancel();
                   }else {
                       multiplier[0] += 0.25;
                       message.editMessageEmbeds(getEmbed(commandEvent.getMember().getEffectiveName(), multiplier[0], bet*multiplier[0])).complete();
                   }
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(task, 1500, 1500);
        }catch (NumberFormatException e) {
            commandEvent.reply("I had trouble parsing you're bet.");
        }catch (InsufficientFundsException e){
            commandEvent.reply(e.getMessage());
        }catch (ServerNotFoundException | AccountNotFoundException e){
            Logger.debug(3, e.getMessage());
        }

    }

    private MessageEmbed getEmbed(String name, double multiplier, double payout){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.BLUE);
        builder.setTitle("Crash - " + name);
        builder.addField("Multiplier: **" + multiplier + "X**", "", false);
        builder.addField("Payout: **" + Utils.round(payout) + "**", "", false);
        return builder.build();
    }

    private static MessageEmbed getWinningEmbed(double multiplier, double payout){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.GREEN);
        builder.setTitle("Crash - Winner");
        builder.addField("Multiplier: **" + multiplier + "X**", "", false);
        builder.addField("Payout: **" + Utils.round(payout) + "**", "", false);
        return builder.build();
    }

    private MessageEmbed crash(String name, double multiplier, double payout){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle(" - " + name);
        builder.addField("Crashed at: **" + multiplier + "X**", "", false);
        builder.addField("Payout: **-" + Utils.round(payout) + "**", "", false);
        return builder.build();
    }

    public static MessageEmbed stop(Message message, Member member){
        runningMessages.remove(message.getId());
        timer.cancel();
        MessageEmbed embed = message.getEmbeds().get(0);
        List<MessageEmbed.Field> fields = embed.getFields();
        double multiplier = parseMultiplier(fields.get(0));
        double payout = parsePayout(fields.get(1));
        message.delete().complete();
        try{
            ServerManager.findServer(message.getGuild().getId()).accountManager.deposit(member.getId(), payout);
        }catch (AccountNotFoundException | ServerNotFoundException e){
            Logger.debug(3, e.getMessage());
        }
        return getWinningEmbed(multiplier, payout);
    }

    private static double parseMultiplier(MessageEmbed.Field field){
        String str = field.getName();
        str = str.replace("Multiplier: **", "");
        str = str.replace("X**", "");
        return Double.parseDouble(str);
    }
    private static double parsePayout(MessageEmbed.Field field){
        String str = field.getName();
        str = str.replace("Payout: **", "");
        str = str.replace("**", "");
        return Double.parseDouble(str);
    }
}
