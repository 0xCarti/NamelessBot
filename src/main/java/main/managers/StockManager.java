package main.managers;

import exceptions.InvalidTickerException;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockManager {
    public static class StockMarket{
        public ArrayList<String> URLS = new ArrayList<>(){{
            add("https://www.google.com/finance/quote/NYT:NYSE");
            add("https://www.google.com/finance/quote/TD:TSE");
            add("https://www.google.com/finance/quote/AMZN:NASDAQ");
            add("https://www.google.com/finance/quote/GOOGL:NASDAQ");
            add("https://www.google.com/finance/quote/MSFT:NASDAQ");
            add("https://www.google.com/finance/quote/AC:TSE");
            add("https://www.google.com/finance/quote/BBD.B:TSE");
            add("https://www.google.com/finance/quote/NFLX:NASDAQ");
            add("https://www.google.com/finance/quote/SQ:NYSE");
            add("https://www.google.com/finance/quote/NVDA:NASDAQ");
            add("https://www.google.com/finance/quote/AMD:NASDAQ");
            add("https://www.google.com/finance/quote/INTC:NASDAQ");
            add("https://www.google.com/finance/quote/BAC:NYSE");
            add("https://www.google.com/finance/quote/WMT:NYSE");
            add("https://www.google.com/finance/quote/V:NYSE");
            add("https://www.google.com/finance/quote/FB:NASDAQ");
            add("https://www.google.com/finance/quote/AAPL:NASDAQ");
            add("https://www.google.com/finance/quote/RBLX:NYSE");
            add("https://www.google.com/finance/quote/TSLA:NASDAQ");
        }};
    }

    public static class Portfolio {
        public List<Stock> stocks = new ArrayList<>();
        public double value;
        public String userId;
        public String name;


    }

    public static class Stock implements Comparable<Stock>{
        public String url;
        public String ticker;
        public String name;
        public double price, previousClose;
        public double change;
        public LocalDateTime lastUpdated;
        public int shares;

        @Override
        public int compareTo(@NotNull StockManager.Stock stock) {
            return Double.compare(this.price, stock.price);
        }
    }

    public static class StockWatcher{
    }
}
