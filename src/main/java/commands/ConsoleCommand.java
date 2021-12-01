package commands;

public abstract class ConsoleCommand {
    public String name = "";
    public String description = "";
    public String syntax = "";

    public abstract void execute();
    public abstract void execute(String[] args);
}
