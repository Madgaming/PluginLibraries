package net.zetaeta.libraries.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * 
 * 
 * @author Zetaeta
 *
 */
public abstract class PluginCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args ) {
        return false;
    }
    
}
