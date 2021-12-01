package main.economy;

public class Withdrawal {
    public String fromUserID;
    public double balance;

    public Withdrawal(String fromUserID, double balance) {
        this.fromUserID = fromUserID;
        this.balance = balance;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Withdrawal{" +
                "fromUserID='" + fromUserID + '\'' +
                ", balance=" + balance +
                '}';
    }
}
