package exceptions;

public class ServerNotFoundException extends Exception{
    public ServerNotFoundException(String message){
        super(message);
    }
}
