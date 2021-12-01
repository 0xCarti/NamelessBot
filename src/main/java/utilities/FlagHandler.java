package utilities;

import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FlagHandler {
    public static final Pattern flagPattern = Pattern.compile("-[a-zA-Z]");
    private static final Pattern urlPattern = Pattern.compile("(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})");
    private static final Pattern localPattern = Pattern.compile("(.+)\\S\\.(mp3|wav|ogg)");
    private static final Pattern searchPatter = Pattern.compile("(yt|sc)search:.+");

    public static List<String> getFlags(String args){
        List<String> flags = new ArrayList<>();
        String[] splitArgs = args.split(" ");
        for (String arg : splitArgs) {
            if (arg.matches(flagPattern.pattern())) {
                flags.add(arg);
            }
        }
        return flags;
    }
    public static List<String> getFlags(CommandEvent commandEvent){
        String args = commandEvent.getMessage().getContentRaw();
        List<String> flags = new ArrayList<>();
        String[] splitArgs = args.split(" ");
        for (String arg : splitArgs) {
            if (arg.matches(flagPattern.pattern())) {
                flags.add(arg);
            }
        }
        return flags;
    }

    public static boolean isUrl(String arg){
        return arg.matches(urlPattern.pattern());
    }
    public static boolean isLocalUrl(String arg){
        return arg.matches(localPattern.pattern());
    }
    public static boolean isSearchUrl(String arg){
        return arg.matches(searchPatter.pattern());
    }

    public static List<String> getArgsList(String args){
        List<String> argsList = new ArrayList<>();
        String[] splitArgs = args.split(" ");
        for (String arg : splitArgs) {
            if (!arg.matches(flagPattern.pattern()) && !arg.startsWith("~")) {
                argsList.add(arg);
            }
        }
        return argsList;
    }
    public static String getArgsString(List<String> args, String delimiter){
        return String.join(delimiter, args);
    }
    public static List<String> getArgsList(CommandEvent commandEvent){
        String args = commandEvent.getMessage().getContentRaw();
        List<String> argsList = new ArrayList<>();
        String[] splitArgs = args.split(" ");
        for (String arg : splitArgs) {
            if (!arg.matches(flagPattern.pattern()) && !arg.startsWith("~")) {
                argsList.add(arg);
            }
        }
        return argsList;
    }
    public static String getArgsString(CommandEvent commandEvent, String delimiter){
        String args = commandEvent.getMessage().getContentRaw();
        return String.join(delimiter, args);
    }
}
