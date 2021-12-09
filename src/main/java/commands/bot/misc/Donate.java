package commands.bot.misc;

import com.jagrosh.jdautilities.command.CommandEvent;
import commands.bot.CustomCommand;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import utilities.Config;

import java.awt.*;

public class Donate extends CustomCommand {
    public Donate() {
        this.name = "Donate";
        this.help = "Get a link to donate to the devs, we really appreciate all the support!";
        this.syntax = "~donate";
        this.category = MainBot.MISC;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Donation Links");
        builder.addField("Paypal", "`" + Config.PAYPAL + "`", false);
        builder.addField("BTC", "`" + Config.BTC + "`", false);
        builder.addField("ETH", "`" + Config.ETH + "`", false);
        builder.setImage("https://c.tenor.com/rTwofXwCx9MAAAAC/thank-luffy.gif");
        builder.setColor(Color.BLUE);
        builder.setFooter("If you want to be personally thanked for you donation feel free to join the support server and I'll say thanks!");
        commandEvent.reply(builder.build());
    }
}
