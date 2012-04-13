package net.zetaeta.plugins.libraries;

import net.zetaeta.plugins.libraries.commands.management.CommandsManager;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Zetaeta
 * 
 * A JavaPlugin with some predefined utility methods, useful (but not required) for using a CommandsManager.
 *
 */
public abstract class ManagedJavaPlugin extends JavaPlugin {
    
    /**
     * Represents the CommandsManager for this plugin.
     */
    protected CommandsManager commandsManager;
    
    /**
     * @param name Name of the command to get
     * @return Command with label <code>name</code>.
     */
    public Command getGenericCommand(String name) {
        return commandsManager.getCommand(name, false);
    }
}
