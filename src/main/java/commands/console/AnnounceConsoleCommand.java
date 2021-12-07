package commands.console;

public class AnnounceConsoleCommand extends ConsoleCommand {

    public AnnounceConsoleCommand() {
        this.name = "Announce";
        this.description = "Makes an announcement on all servers.";
        this.syntax = "/announce";
    }

    @Override
    public void execute() {
        System.out.println("This doesn't work, bryce is too tired.");
    }

    @Override
    public void execute(String[] args) {
        execute();
    }
}