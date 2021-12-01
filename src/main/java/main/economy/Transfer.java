package main.economy;

public class Transfer {
    public String toUserID;
    public String fromUserID;
    public double balance;

    public Transfer(String toUserID, String fromUserID, double balance) {
        this.toUserID = toUserID;
        this.fromUserID = fromUserID;
        this.balance = balance;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
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
        return "Transfer{" +
                "toUserID='" + toUserID + '\'' +
                ", fromUserID='" + fromUserID + '\'' +
                ", balance=" + balance +
                '}';
    }
}
