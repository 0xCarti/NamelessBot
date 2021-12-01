package utilities;

import main.managers.ConsoleCommandManager;
import utilities.Logger;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleCommandThread extends Thread{
    @Override
    public void run() {
        final Scanner kyb = new Scanner(System.in);
        while (true) {
            if(kyb.hasNextLine()) {
                String command = kyb.nextLine();
                if(command.startsWith("/") && command.length() > 1) {
                    String[] args = command.substring(1).split(" ");
                    if(args.length > 1) {
                        ConsoleCommandManager.executeCommand(args[0], Arrays.copyOfRange(args, 1, args.length));
                    }else{
                        ConsoleCommandManager.executeCommand(args[0]);
                    }
                }else{
                    Logger.debug(3, "Command not recognized, use /help for a list of commands.");
                }
            }
        }
    }
}
