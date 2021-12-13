package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private static final CharSequence flags = "\\[]";
    private static boolean debug = Config.DEBUG;

    //1 = info, cyan
    //2 = command output, green
    //3 = error, red

    public static void debug(String output){
        if (debug){
            System.out.println(output);
        }
    }

    private static void log(String output){
        LocalDateTime time = LocalDateTime.now();
        String timeString = "[" + time.format(dateFormat) + "] ";
        System.out.println(timeString + output);
    }
    @SafeVarargs
    public static <T> void log(String output, T... args){
        List<T> list = asList(args);
        log(replace(output, list));
    }
    @SafeVarargs
    public static <T> void log(int flag, String output, T... args){
        switch (flag){
            case 1: output = Utils.ANSI_CYAN + output + Utils.ANSI_RESET;
                break;
            case 2: output = Utils.ANSI_GREEN + output + Utils.ANSI_RESET;
                break;
            case 3: output = Utils.ANSI_RED + output + Utils.ANSI_RESET;
                break;
        }
        log(output, args);
    }

    public static void debug(int flag, String output){
        if (debug){
            log(flag, output);
        }
    }
    @SafeVarargs
    public static <T> void debug(String output, T... args){
        if (debug){
            log(output, args);
        }
    }
    @SafeVarargs
    public static <T> void debug(int flag, String output, T... args){
        if (debug){
            log(flag, output, args);
        }
    }

    public static void error(String output){
        log(3, output);
    }
    @SafeVarargs
    public static <T> void error(String output, T... args){
        log(3, output, args);
    }

    private static <T> String replace(String input, List<T> args){
        if (!input.contains("[]") || args.isEmpty()){
            return input;
        }
        try{
            input = input.replaceFirst(flags.toString(), args.remove(0).toString());
        }catch (IllegalArgumentException e){
            Logger.debug(3, e.getMessage());
        }
        return replace(input, args);
    }
    private static <T> List<T> asList(T[] elements){
        List<T> list = new ArrayList<>();
        for (T i : elements){
            list.add(i);
        }
        return list;
    }
}
