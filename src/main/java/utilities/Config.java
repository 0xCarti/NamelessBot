package utilities;

import java.io.*;

public class Config {
    public static String PREFIX;
    public static String LIVE_TOKEN;
    public static String TEST_TOKEN;
    public static String OPENAI_KEY;
    public static String STEAM_KEY;
    public static String OWNER_ID;
    public static String INVITE;
    public static boolean DEBUG;

    public Config(){
        InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream("configuration");
        if (ioStream == null) {
            throw new IllegalArgumentException("Configuration is not found");
        }
        try {
            InputStreamReader ioStreamReader = new InputStreamReader(ioStream);
            BufferedReader br = new BufferedReader(ioStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                String setting = getKey(line);
                String value = getValue(line);
                if(setting.equals("") || value.equals("")){
                    continue;
                }
                switch (setting.toUpperCase()) {
                    case "PREFIX" -> PREFIX = value;
                    case "LIVE_TOKEN" -> LIVE_TOKEN = value;
                    case "TEST_TOKEN" -> TEST_TOKEN = value;
                    case "OPENAI_KEY" -> OPENAI_KEY = value;
                    case "STEAMAPI_KEY" -> STEAM_KEY = value;
                    case "OWNER_ID" -> OWNER_ID = value;
                    case "INVITE" -> INVITE = value;
                    case "DEBUG" -> DEBUG = Boolean.parseBoolean(value);
                    default -> {
                        System.out.println("Unknown setting: " + setting);
                        System.exit(1);
                    }
                }
            }
            br.close();
            ioStreamReader.close();
            if(LIVE_TOKEN.equals("") || PREFIX.equals("") || OWNER_ID.equals("")){
                Logger.debug(3, "Configuration is not complete - LIVE_TOKEN, PREFIX AND OWNER ID ARE REQUIRED");
                System.exit(1);
            }
            Logger.debug(2, "Configuration loaded successfully.");
        } catch (IOException e) {
            Logger.debug(3, "IOException thrown in the config file.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private String getKey(String line){
        String[] split = line.split("\\[");
        if(split.length == 1) return "";
        return split[0];
    }
    private String getValue(String line){
        if(!line.contains("[")){
            return "";
        }
        String[] split = line.split("\\[");
        split = split[1].split("\\]");
        return split[0];
    }
}
