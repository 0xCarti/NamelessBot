package main.economy;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import utilities.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SlotMachine {
    private final List<String> symbols = new ArrayList(){{
        add(":poop:");
        add(":poop:");
        add(":poop:");
        add(":poop:");
        add(":poop:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":pound:");
        add(":pound:");
        add(":pound:");
        add(":pound:");
        add(":pound:");
        add(":pound:");
        add(":moneybag:");
        add(":moneybag:");
        add(":moneybag:");
        add(":moneybag:");
        add(":gem:");
        add(":gem:");
        add(":gem:");
    }};
    private final List<String> payoutSymbols = new ArrayList(){{
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":dollar:");
        add(":pound:");
        add(":pound:");
        add(":pound:");
        add(":moneybag:");
        add(":moneybag:");
        add(":gem:");
    }};
    private final String[][] symbolTable = new String[3][3];
    private Message slotMessage = null;
    private int payoutCount = 0;
    private int payoutNum = 25;


    public SlotMachine(){
        fillTable();
    }

    public void display(CommandEvent commandEvent){
        slotMessage = commandEvent.getChannel().sendMessage(getMessageEmbed()).complete();
    }
    public double spin(){
        payoutCount++;

        for(int i = 0; i < 9; i++){
            fillTable();
            slotMessage.editMessage(getMessageEmbed()).complete();
        }

        if(payoutCount == payoutNum){
            payoutNum = Utils.getRandomInt(50) - Utils.getRandomInt(30);
            fillPayoutTable();
            slotMessage.editMessage(getMessageEmbed()).complete();
            return getMultiplier();
        }else{
            return getMultiplier();
        }
    }
    public void reset(){
        slotMessage.delete().queueAfter(1, TimeUnit.MINUTES);
        slotMessage = null;
    }

    private void fillTable(){
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                symbolTable[r][c] = getSymbol();
            }
        }
    }
    private void fillPayoutTable(){
        String symbol = payoutSymbols.get(Utils.getRandomInt(payoutSymbols.size()));
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                symbolTable[r][c] = symbol;
            }
        }
    }
    private MessageEmbed getMessageEmbed(){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("Kasiurak Slots");
        embedBuilder.setDescription("Play quick game of slaps if you're feeling lucky.");

        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                embedBuilder.addField("", symbolTable[r][c], true);
            }
        }

        return embedBuilder.build();
    }
    private double getMultiplier(){
        double multiplier = 0;

        for(int i = 1; i <= 3; i++){
            Row row = getRow(i);
            if(row.isWinner()){
                switch (row.getSymbolOne()) {
                    case ":dollar:" -> multiplier += .5;
                    case ":pound:" -> multiplier += 2;
                    case ":moneybag:" -> multiplier += 2.5;
                    case ":gem:" -> multiplier += 5;
                    default -> {multiplier += 0;}
                }
            }
        }
        for(int i = 1; i <= 3; i++){
            Column col = getColumn(i);
            if(col.isWinner()){
                switch (col.getSymbolOne()) {
                    case ":dollar:" -> multiplier += .5;
                    case ":yen:" -> multiplier += 1;
                    case ":euro:" -> multiplier += 1.25;
                    case ":pound:" -> multiplier += 1.5;
                    case ":moneybag:" -> multiplier += 2;
                    case ":gem:" -> multiplier += 5;
                    default -> {multiplier += 0;}
                }
            }
        }
        for(int i = 1; i <= 2; i++){
            Diagonal diagonal = getDiagonal(i);
            if(diagonal.isWinner()){
                switch (diagonal.getSymbolOne()) {
                    case ":dollar:" -> multiplier += .5;
                    case ":yen:" -> multiplier += 1;
                    case ":euro:" -> multiplier += 1.25;
                    case ":pound:" -> multiplier += 1.5;
                    case ":moneybag:" -> multiplier += 2;
                    case ":gem:" -> multiplier += 5;
                    default -> {multiplier += 0;}
                }
            }
        }

        return multiplier;
    }
    private Row getRow(int rowNumber){
        List<String> symbols = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            symbols.add(symbolTable[rowNumber-1][i]);
        }
        return new Row(symbols);
    }
    private Column getColumn(int colNumber){
        List<String> symbols = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            symbols.add(symbolTable[i][colNumber-1]);
        }
        return new Column(symbols);
    }
    private Diagonal getDiagonal(int diagonalNumber){
        switch (diagonalNumber){
            case 1:
                return new Diagonal(symbolTable[0][0], symbolTable[1][1], symbolTable[2][2]);
            case 2:
                return new Diagonal(symbolTable[2][0], symbolTable[1][1], symbolTable[0][2]);
        }
        return null;
    }
    private String getSymbol(){
        return symbols.get(Utils.getRandomInt(symbols.size()));
    }

    //Nested subclasses for organizing rows, columns and diagonals
    private class Row{
        private String symbolOne, symbolTwo, symbolThree;

        public Row(String symbolOne, String symbolTwo, String symbolThree) {
            this.symbolOne = symbolOne;
            this.symbolTwo = symbolTwo;
            this.symbolThree = symbolThree;
        }

        public Row(List<String> symbols){
            this.symbolOne = symbols.get(0);
            this.symbolTwo = symbols.get(1);
            this.symbolThree = symbols.get(2);
        }

        public String getSymbolOne() {
            return symbolOne;
        }

        public void setSymbolOne(String symbolOne) {
            this.symbolOne = symbolOne;
        }

        public String getSymbolTwo() {
            return symbolTwo;
        }

        public void setSymbolTwo(String symbolTwo) {
            this.symbolTwo = symbolTwo;
        }

        public String getSymbolThree() {
            return symbolThree;
        }

        public void setSymbolThree(String symbolThree) {
            this.symbolThree = symbolThree;
        }

        public boolean isWinner(){
            return (symbolOne.equals(symbolTwo) && symbolTwo.equals(symbolThree));
        }
    }
    private class Column{
        private String symbolOne, symbolTwo, symbolThree;

        public Column(String symbolOne, String symbolTwo, String symbolThree) {
            this.symbolOne = symbolOne;
            this.symbolTwo = symbolTwo;
            this.symbolThree = symbolThree;
        }

        public Column(List<String> symbols){
            this.symbolOne = symbols.get(0);
            this.symbolTwo = symbols.get(1);
            this.symbolThree = symbols.get(2);
        }

        public String getSymbolOne() {
            return symbolOne;
        }

        public void setSymbolOne(String symbolOne) {
            this.symbolOne = symbolOne;
        }

        public String getSymbolTwo() {
            return symbolTwo;
        }

        public void setSymbolTwo(String symbolTwo) {
            this.symbolTwo = symbolTwo;
        }

        public String getSymbolThree() {
            return symbolThree;
        }

        public void setSymbolThree(String symbolThree) {
            this.symbolThree = symbolThree;
        }

        public boolean isWinner(){
            return (symbolOne.equals(symbolTwo) && symbolTwo.equals(symbolThree));
        }
    }
    private class Diagonal {
        private String symbolOne, symbolTwo, symbolThree;

        public Diagonal(String symbolOne, String symbolTwo, String symbolThree) {
            this.symbolOne = symbolOne;
            this.symbolTwo = symbolTwo;
            this.symbolThree = symbolThree;
        }

        public String getSymbolOne() {
            return symbolOne;
        }

        public void setSymbolOne(String symbolOne) {
            this.symbolOne = symbolOne;
        }

        public String getSymbolTwo() {
            return symbolTwo;
        }

        public void setSymbolTwo(String symbolTwo) {
            this.symbolTwo = symbolTwo;
        }

        public String getSymbolThree() {
            return symbolThree;
        }

        public void setSymbolThree(String symbolThree) {
            this.symbolThree = symbolThree;
        }

        public boolean isWinner(){
            return (symbolOne.equals(symbolTwo) && symbolTwo.equals(symbolThree));
        }
    }
}
