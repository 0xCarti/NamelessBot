package commands;

import com.jagrosh.jdautilities.command.Command;

public abstract class CustomCommand extends Command {
    protected String syntax = "null";

    public String getSyntax() {
        return syntax;
    }
}
