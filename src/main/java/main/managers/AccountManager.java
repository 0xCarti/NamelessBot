package main.managers;

import com.jagrosh.jdautilities.command.CommandEvent;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    public final List<Account> accounts;

    public AccountManager(){
        this(new ArrayList<>());
    }
    public AccountManager(List<Account> accounts) {
        this.accounts = accounts;
        ServerManager.save();
    }

    public Account findAccount(String userID) throws AccountNotFoundException {
        for (Account account : accounts) {
            if(account.userID.equals(userID)){
                return account;
            }
        }
        throw new AccountNotFoundException("Could not find an account associated with userID [" + userID + "]");
    }

    public void add(String userID, String name, double balance, double exp, int level) {
        Account account = new Account(userID, name, balance, exp, level);
        accounts.add(account);
        ServerManager.save();
    }
    public void add(String userID, String name) {
        this.add(userID, name, 0.0, 0.0, 1);
    }
    public void remove(String userID) throws AccountNotFoundException {
        Account account = findAccount(userID);
        accounts.remove(account);
        ServerManager.save();
    }

    public void setAccountBalance(String userID, double balance) throws AccountNotFoundException {
        Account account = findAccount(userID);
        account.setBalance(balance);
    }
    public double getAccountBalance(String userID) throws AccountNotFoundException {
        Account account = findAccount(userID);
        return account.balance;
    }

    public void deposit(String userID, double balance) throws AccountNotFoundException {
        Account account = findAccount(userID);
        account.deposit(balance);
    }
    public void depositAll(double balance) {
        for (Account account : accounts) {
            account.deposit(balance);
        }
    }
    public void withdraw(String userID, double balance) throws AccountNotFoundException, InsufficientFundsException {
        Account account = findAccount(userID);
        account.withdraw(balance);
    }
    public void transfer(String fromUserID, String toUserID, double balance) throws AccountNotFoundException, InsufficientFundsException {
        Account fromAccount = findAccount(fromUserID);
        fromAccount.withdraw(balance);
        Account toAccount = findAccount(toUserID);
        toAccount.deposit(balance);
    }

    public static class Account{
        public String userID;
        public String name;
        public double balance;
        public double exp;
        public int level;

        public Account(String userID, String name, double balance, double exp, int level){
            this.userID = userID;
            this.name = name;
            this.balance = balance;
            this.exp = exp;
            this.level = level;
            ServerManager.save();
        }
        public Account(String userID, String name){
            this(userID, name, 0.0, 0, 1);
            ServerManager.save();
        }

        public void deposit(double balance){
            this.balance += balance;
            ServerManager.save();
        }
        public void withdraw(double balance) throws InsufficientFundsException {
            if (this.balance >= balance){
                this.balance -= balance;
                ServerManager.save();
            }else{
                throw new InsufficientFundsException("Insufficient Funds");
            }
        }

        public void giveEXP(double exp){
            this.exp += exp;
            if(level < generateLevel(this.exp)){
                balance += (generateLevel(this.exp) - level) * 1000.0;
                level = generateLevel(this.exp);
            }
            ServerManager.save();
        }
        public double generateEXP(GenericEvent genericEvent){
            double multiplier = 0.0;
            int messageCount = 0;

            if(genericEvent instanceof CommandEvent event){
                multiplier = Utils.getRandomDouble(1, 2);
                messageCount = event.getMessage().getContentRaw().length();
            }else if(genericEvent instanceof MessageReceivedEvent event){
                multiplier = Utils.getRandomDouble(1);
                messageCount = event.getMessage().getContentRaw().length();
            }
            return multiplier*messageCount;
        }
        public int generateLevel(double EXP){
            return (int) (.3 * Math.sqrt(EXP));
        }

        public void setUserID(String userID) {
            this.userID = userID;
            ServerManager.save();
        }
        public void setName(String name) {
            this.name = name;
            ServerManager.save();
        }
        public void setBalance(double balance) {
            this.balance = balance;
            ServerManager.save();
        }
        public void setExp(double exp) {
            this.exp = exp;
            ServerManager.save();
        }
        public void setLevel(int level) {
            this.level = level;
            ServerManager.save();
        }
    }
}
