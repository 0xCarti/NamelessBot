package main.managers;

import commands.bot.steam.csgo.Stats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.logging.Log;
import org.steambuff.SteamApi;
import org.steambuff.exception.SteamApiException;
import org.steambuff.method.SteamId;
import org.steambuff.method.steamuser.entity.PlayerSummaries;
import org.steambuff.method.steamuser.entity.ProgressGame;
import org.steambuff.method.steamuser.entity.StatsGame;
import org.steambuff.method.steamuser.entity.UserStats;
import utilities.Config;
import utilities.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class SteamManager {
    public static final int CSGO_ID = 730;
    public static final SteamApi steam = SteamApi.getInstance(Config.STEAM_KEY);
    public static final Pattern STEAM_ID_PATTERN = Pattern.compile("(?:https?:\\/\\/)?steamcommunity\\.com\\/(?:profiles|id)\\/[a-zA-Z0-9]+");

    public static MessageEmbed getCsgoUserStats(String steamId) throws SteamApiException, MalformedURLException {
        SteamId user;
        if(steamId.matches(STEAM_ID_PATTERN.pattern())){
            user = getIdFromUrl(steamId);
        }else{
            user = new SteamId(steamId);
        }
        UserStats stats = steam.getSteamUserInterface().getUserStatsForGame(user, CSGO_ID);
        ProgressGame progress = stats.getProgressGame();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("CSGO Stats - " + getNameFromId(user));
        StatsGame kills = progress.getStatByName("total_kills");
        StatsGame deaths = progress.getStatByName("total_deaths");
        StatsGame headshots = progress.getStatByName("total_kills_headshot");
        StatsGame wins = progress.getStatByName("total_wins");
        StatsGame defuses = progress.getStatByName("total_defused_bombs");
        StatsGame plants = progress.getStatByName("total_planted_bombs");
        builder.addField("Kills", "`" + String.valueOf(kills.getValue()) + "`", true);
        builder.addField("Deaths", "`" + String.valueOf(deaths.getValue()) + "`", true);
        builder.addField("Headshots", "`" + String.valueOf(headshots.getValue()) + "`", true);
        builder.addField("Wins", "`" + String.valueOf(wins.getValue()) + "`", true);
        builder.addField("Plants", "`" + String.valueOf(plants.getValue()) + "`", true);
        builder.addField("Defuses", "`" + String.valueOf(defuses.getValue()) + "`", true);
        List<StatsGame> weapons = getTopGuns(progress);
        for(int i = 0; i < 3; i++) {
            StatsGame weapon = weapons.get(i);
            String name = weapon.getNameStat().split("_")[weapon.getNameStat().split("_").length - 1];
            builder.addField(name.toUpperCase() + " Kills", "`" + weapon.getValue() + "`", true);
        }
        builder.setFooter("Rating: " + getRating(kills.getValue(), deaths.getValue(), headshots.getValue(), defuses.getValue(), plants.getValue()));
        return builder.build();
    }
    public static MessageEmbed getUserStats(String steamId) throws SteamApiException, MalformedURLException {
        SteamId user;
        if(steamId.matches(STEAM_ID_PATTERN.pattern())){
            user = getIdFromUrl(steamId);
        }else{
            user = new SteamId(steamId);
        }
        PlayerSummaries summaries = steam.getSteamUserInterface().getPlayerSummaries(user).orElseThrow();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User Stats - " + summaries.getDisplayName());
        builder.setFooter("" + summaries.getProfileUrl());
        builder.setImage(summaries.getAvatarFull());
        builder.addField("SteamID", "`" + summaries.getSteamId().toId64() + "`", true);
        builder.addField("Last Online", "`" + summaries.getLastLogOff() + "`", true);
        builder.addField("Status", "`" + summaries.getPersonaState() + "`", true);
        return builder.build();
    }

    private static List<StatsGame> getTopGuns(ProgressGame progress){
        List<StatsGame> stats = new ArrayList<>();
        for(StatsGame stat : progress.getStatsList()){
            if(stat.getNameStat().contains("total_kills") && !stat.getNameStat().contains("headshot") && !stat.getNameStat().equals("total_kills")){
                stats.add(stat);
            }
        }
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for(int i = 0; i < stats.size()-1; i++) {
                if(stats.get(i).getValue() < stats.get(i+1).getValue()) {
                    swapped = true;
                    StatsGame temp = stats.get(i);
                    stats.set(i, stats.get(i+1));
                    stats.set(i+1, temp);
                }
            }
        }
        return stats;
    }
    private static String getRating(long kills, long deaths, long headshots, long defuses, long plants) {
        double kd = ((double)kills/deaths) + ((double)headshots/kills);
        double etc = ((double)plants + (double)defuses);
        if(kd + Math.sqrt(etc) <= 10 ){
            return "F-";
        }else if(kd + Math.sqrt(etc) <= 20){
            return "F+";
        }else if(kd + Math.sqrt(etc) <= 30){
            return "D-";
        }else if(kd + Math.sqrt(etc) <= 40){
            return "D+";
        }else if(kd + Math.sqrt(etc) <= 50){
            return "C-";
        }else if(kd + Math.sqrt(etc) <= 60){
            return "C+";
        }else if(kd + Math.sqrt(etc) <= 70){
            return "B-";
        }else if(kd + Math.sqrt(etc) <= 80){
            return "B+";
        }else if(kd + Math.sqrt(etc) <= 90){
            return "A-";
        }else if(kd + Math.sqrt(etc) <= 100){
            return "A+";
        }else{
            return "S+";
        }
    }
    private static SteamId getIdFromUrl(String url) throws MalformedURLException {
        SteamId id = steam.getSteamAdditionalUtility().getSteamIdByPage(new URL(url));
        Logger.debug(3, "SteamID: " + id.toId64());
        return id;
    }
    private static String getNameFromId(SteamId user) throws SteamApiException {
        return steam.getSteamUserInterface().getPlayerSummaries(user).orElseThrow().getDisplayName();
    }
}
