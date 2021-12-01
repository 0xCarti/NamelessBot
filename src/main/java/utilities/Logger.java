package utilities;

import java.util.ArrayList;
import java.util.List;

public class Logger {
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

    public static void debug(int flag, String output){
        switch (flag){
            case 1: output = Utils.ANSI_CYAN + output + Utils.ANSI_RESET;
                    break;
            case 2: output = Utils.ANSI_GREEN + output + Utils.ANSI_RESET;
                    break;
            case 3: output = Utils.ANSI_RED + output + Utils.ANSI_RESET;
                    break;
        }
        if (debug){
            System.out.println(output);
        }
    }

    public static <T> void debug(String output, T... args){
        if (debug){
            List<T> list = asList(args);
            System.out.println(replace(output, list));
        }
    }

    public static <T> void debug(int flag, String output, T... args){
        switch (flag){
            case 1: output = Utils.ANSI_CYAN + output + Utils.ANSI_RESET;
                break;
            case 2: output = Utils.ANSI_GREEN + output + Utils.ANSI_RESET;
                break;
            case 3: output = Utils.ANSI_RED + output + Utils.ANSI_RESET;
                break;
        }
        if (debug){
            List<T> list = asList(args);
            System.out.println(replace(output, list));
        }
    }

    public static <T> String replace(String input, List<T> args){
        if (!input.contains("[]") || args.isEmpty()){
            return input;
        }
        input = input.replaceFirst(flags.toString(), args.remove(0).toString());
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
