package main.managers;

import exceptions.InvalidTickerException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Config;
import utilities.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class StockManager {
    public List<Portfolio> portfolios;

    public StockManager() {
        this.portfolios = new ArrayList<>();
    }

    public void addPortfolio(String userId, String name){
        portfolios.add(new Portfolio(userId, name));
    }

    public void removePortfolio(String userId){
        for(Portfolio p : portfolios){
            if(p.userId.equals(userId)){
                portfolios.remove(p);
                return;
            }
        }
    }

    public Portfolio getPortfolio(String userId){
        for(Portfolio p : portfolios){
            if(p.userId.equals(userId)){
                return p;
            }
        }
        return null; //throw portfolio not found exception
    }

    public class Portfolio {
        public List<Stock> stocks = new ArrayList<>();
        public String userId;
        public String name;

        public Portfolio(String userId, String name) {
            this.userId = userId;
            this.name = name;
        }

        public void addStock(String ticker, int amount) throws InvalidTickerException, IOException {
            Stock stock;
            try{
                stock = findStock(ticker);
                stock.price = getStockPrice(ticker);
                stock.spent += stock.price * amount;
                stock.shares += amount;
            }catch (InvalidTickerException e){
                stock = new Stock(ticker);
                stock.price = getStockPrice(ticker);
                stock.spent = stock.price * amount;
                stock.shares = amount;
                stocks.add(stock);
            }
        }

        public Stock findStock(String ticker) throws InvalidTickerException {
            for (Stock stock : stocks) {
                if (stock.ticker.equals(ticker)) {
                    return stock;
                }
            }
            throw new InvalidTickerException("Ticker not found in portfolio"); //throw stock not found exception
        }

        public double removeStock(String ticker, int amount) throws InvalidTickerException, IOException {
            Stock stock = findStock(ticker);
            if (stock.shares >= amount) {
                double gained = stock.price * amount;
                if(stock.shares - amount <= 0){
                    stocks.remove(stock);
                }else{
                    stock.shares -= amount;
                }
                return gained;
            }else{
                return 0;
            }
        }

        public MessageEmbed getPortfolioEmbed(){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(name);
            eb.setColor(Color.GREEN);
            eb.setDescription("Portfolio");
            for (Stock stock : stocks) {
                stock.update();
                eb.addField(stock.ticker, "`$" + stock.price + "`", true);
                eb.addField("Shares", "`" + stock.shares + "`", true);
                eb.addField("Spent", "`$" + stock.spent + "`", true);
            }
            return eb.build();
        }
    }

    public static class Stock implements Comparable<Stock>{
        public String ticker;
        public double price;
        public double spent;
        public int shares;

        public Stock(String ticker){
            this.ticker = ticker;
        }

        public void update(){
            try {
                price = getStockPrice(ticker);
            } catch (IOException | InvalidTickerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int compareTo(@NotNull StockManager.Stock stock) {
            return Double.compare(this.spent, stock.spent);
        }
    }

    public static MessageEmbed getQuoteEmbed(String ticker) throws InvalidTickerException, IOException {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.CYAN);
        builder.setTitle("$" + ticker.toUpperCase());
        Process p = Runtime.getRuntime().exec("python StockWatchingScript/GetQuote.py " + Config.STOCKAPI_KEY + " " + ticker.toUpperCase());
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String output = stdInput.readLine();
        String[] firstSplit = output.split(",");
        for(String string : firstSplit){
            String[] secondSplit = string.split(":");
            switch (secondSplit[0]){
                case "c" -> builder.setTitle("$" + ticker.toUpperCase() + " - " + secondSplit[1]);
                case "d" -> builder.addField("Change", "`" + secondSplit[1] + "`", true);
                case "dp" -> builder.addField("Percent Change", "`" + secondSplit[1] + "%`", true);
                case "h" -> builder.addField("High", "`" + secondSplit[1] + "`", true);
                case "l" -> builder.addField("Low", "`" + secondSplit[1] + "`", true);
                case "o" -> builder.addField("Previous Open", "`" + secondSplit[1] + "`", true);
                case "pc" -> builder.addField("Previous Close", "`" + secondSplit[1] + "`", true);
                case "t" -> {
                    long seconds = Long.parseLong(secondSplit[1]);
                    LocalDateTime date = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
                    builder.setFooter("Last updated on " + date.format(format), null);
                }
            }
        }
        return builder.build();
    }
    public static MessageEmbed getCompanyEmbed(String ticker) throws InvalidTickerException, IOException {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.CYAN);
        builder.setTitle("$" + ticker.toUpperCase());
        Process p = Runtime.getRuntime().exec("python StockWatchingScript/GetCompanyProfile.py " + Config.STOCKAPI_KEY + " " + ticker.toUpperCase());
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String output = stdInput.readLine();
        String[] firstSplit = output.split(",");
        for(String string : firstSplit){
            string = string.replaceFirst(":", "`");
            String[] secondSplit = string.split("`");
            switch (secondSplit[0]){
                case "exchange" -> {
                    builder.addField("Exchange", "`" + secondSplit[1] + "`", true);
                }
                case "ipo" -> builder.addField("IPO Date", "`" + secondSplit[1] + "`", true);
                case "marketCapitalization" -> {
                    Locale usa = new Locale("en", "US");
                    Currency dollars = Currency.getInstance(usa);
                    NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);
                    builder.addField("Market Cap", "`" + dollarFormat.format(Double.parseDouble(secondSplit[1])) + "`", true);
                }
                case "name" -> builder.setTitle("$" + ticker.toUpperCase() + " - " + secondSplit[1]);
                case "shareOutstanding" -> builder.addField("Outstanding Shares", "`" + secondSplit[1] + "`", true);
                case "finnhubIndustry" -> builder.addField("Industry", "`" + secondSplit[1] + "`", true);
            }
        }
        return builder.build();
    }
    public static double getStockPrice(String ticker) throws InvalidTickerException, IOException {
        Process p = Runtime.getRuntime().exec("python StockWatchingScript/GetQuote.py " + Config.STOCKAPI_KEY + " " + ticker.toUpperCase());
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String output = stdInput.readLine();
        String[] firstSplit = output.split(",");
        Stock stock = new Stock(ticker);
        for(String string : firstSplit){
            String[] secondSplit = string.split(":");
            if(secondSplit[0].equals("c")){
                return Double.parseDouble(secondSplit[1]);
            }
        }
        throw new InvalidTickerException("Invalid ticker");
    }
}
