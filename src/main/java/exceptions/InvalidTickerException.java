package exceptions;

public class InvalidTickerException extends Exception {
    public InvalidTickerException(String message) {
        super(message);
    }
}