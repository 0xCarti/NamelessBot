package utilities;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import main.MainBot;
import net.dv8tion.jda.api.entities.Message;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static Random rnd = new Random();

    public static int getRandomInt(int max) {
        return rnd.nextInt(max);
    }
    public static int getRandomInt(int min, int max) {
        return rnd.nextInt(min, max);
    }
    public static double getRandomDouble(double max) {
        return rnd.nextDouble(max);
    }
    public static double getRandomDouble(double min, double max) {
        return rnd.nextDouble(min, max);
    }

    public static String[] getArgs(CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split(" ");
        args = Arrays.copyOfRange(args, 1, args.length);
        return args;
    }

    public static String Array2String(String[] array) {
        String output = "";
        for (int i = 0; i < array.length; i++) {
            output += array[i] + " ";
        }
        return output.trim();
    }

    public static String getDateTimeHour() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void enableConsoleColour() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            // Set output mode to handle virtual terminal sequences
            Function GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");
            WinDef.DWORD STD_OUTPUT_HANDLE = new WinDef.DWORD(-11);
            WinNT.HANDLE hOut = (WinNT.HANDLE) GetStdHandleFunc.invoke(WinNT.HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});

            WinDef.DWORDByReference p_dwMode = new WinDef.DWORDByReference(new WinDef.DWORD(0));
            Function GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
            GetConsoleModeFunc.invoke(WinDef.BOOL.class, new Object[]{hOut, p_dwMode});

            int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
            WinDef.DWORD dwMode = p_dwMode.getValue();
            dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
            Function SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
            SetConsoleModeFunc.invoke(WinDef.BOOL.class, new Object[]{hOut, dwMode});
        }
    }

    public static List<Message> reverseList(List<Message> list) {
        List<Message> tmp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tmp.add(list.remove(list.size() - 1));
        }
        return tmp;

    }

    public static double getEXPForNextLevel(double level){
        level *= 10.0;
        level /= 3.0;
        return Math.pow(level, 2.0);
    }

    public static String round(Double num){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(num);
    }
}
