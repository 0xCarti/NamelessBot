package main.economy;

public class Deposit {
    public String toUserID;
    public double balance;

    public Deposit(String toUserID, double balance) {
        this.toUserID = toUserID;
        this.balance = balance;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "toUserID='" + toUserID + '\'' +
                ", balance=" + balance +
                '}';
    }
}
