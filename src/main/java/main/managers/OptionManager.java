package main.managers;

public class OptionManager {
    public boolean economy;
    public boolean audio;
    public boolean exp;
    public boolean steam;
    public boolean dev;

    public OptionManager() {
        this.economy = false;
        this.audio = false;
        this.exp = false;
        this.steam = false;
        this.dev = false;
        ServerManager.save();
    }
    public OptionManager(boolean economy, boolean audio, boolean exp, boolean steam, boolean dev) {
        this.economy = economy;
        this.audio = audio;
        this.exp = exp;
        this.steam = steam;
        this.dev = dev;
        ServerManager.save();
    }

    public void enableAll(){
        this.economy = true;
        this.audio = true;
        this.exp = true;
        this.steam = true;
        this.dev = true;
        ServerManager.save();
    }

    public void disableAll(){
        this.economy = false;
        this.audio = false;
        this.exp = false;
        this.steam = false;
        this.dev = false;
        ServerManager.save();
    }
}
