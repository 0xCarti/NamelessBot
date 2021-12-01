package exceptions;

public class AccountManagerNotFoundException extends Exception{
    AccountManagerNotFoundException(String message){
        super(message);
    }
}