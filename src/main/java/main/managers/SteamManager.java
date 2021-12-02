package main.managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.steambuff.SteamApi;
import org.steambuff.exception.SteamApiException;
import org.steambuff.method.SteamId;
import org.steambuff.method.steamuser.entity.PlayerSummaries;
import org.steambuff.method.steamuser.entity.ProgressGame;
import org.steambuff.method.steamuser.entity.StatsGame;
import org.steambuff.method.steamuser.entity.UserStats;
import utilities.Config;

import java.util.Locale;
import java.util.Optional;

public class SteamManager {
    public static final int CSGO_ID = 730;
    public static final SteamApi steam = SteamApi.getInstance(Config.STEAM_KEY);

    public static MessageEmbed getCsgoUserStats(String steamId) throws SteamApiException {
        SteamId user = new SteamId(steamId);
        UserStats stats = steam.getSteamUserInterface().getUserStatsForGame(user, CSGO_ID);
        ProgressGame progress = stats.getProgressGame();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("CSGO Stats - " + steamId);
        for(StatsGame stat : progress.getStatsList()) {
            String name = stat.getNameStat().replace("_", " ").toUpperCase();
            String value = "`" + stat.getValue() + "`";
            builder.addField(name, value, true);
        }
        return builder.build();
    }
    public static MessageEmbed getUserStats(String steamId) throws SteamApiException {
        SteamId user = new SteamId(steamId);
        PlayerSummaries summaries = steam.getSteamUserInterface().getPlayerSummaries(user).orElseThrow();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User Stats - " + summaries.getDisplayName());
        builder.setFooter("" + summaries.getProfileUrl());
        builder.setImage(summaries.getAvatarFull());
        builder.addField("SteamID", "`" + summaries.getSteamId().getId() + "`", true);
        builder.addField("Last Online", "`" + summaries.getLastLogOff() + "`", true);
        builder.addField("Status", "`" + summaries.getPersonaState() + "`", true);
        return builder.build();
    }
}
