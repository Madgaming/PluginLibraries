package net.zetaeta.plugins.libraries.commands.local;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class providing command management within a plugin based on the
 * Settlement model. 
 * 
 * @author Zetaeta
 */
public class LocalCommandExecutor {
    protected Map<String, LocalCommandExecutor> subCommands;
    protected String[] usage;
    protected String[] aliases;
    protected LocalPermission permission;
    protected LocalCommandExecutor parent;
    
    
    /**
     * Gets the parent LocalCommandExecutor in the command tree.
     * 
     * @return parent LocalCommandExecutor
     */
    public LocalCommandExecutor getParent() {
        return parent;
    }
    
    
    /**
     * Gets the registered subcommands' executors
     * 
     * @return Set of LocalCommandExecutors registered as subcommands.
     */
    public Set<LocalCommandExecutor> getSubCommands() {
        return new HashSet<LocalCommandExecutor>(subCommands.values());
    }
    
    
    /**
     * Gets the LocalPermission for this command.
     * 
     * @return LocalPermission required for this command.
     */
    public LocalPermission getPermission() {
        return permission;
    }
    
    
    /**
     * Gets the usage list for the command, used for improperly formed command or {@literal /settlement help <command>}
     * 
     * @return Command's usage info, in String[] form ready for {@link org.bukkit.command.CommandSender#sendMessage(String[]) sendMessage()}.
     */
    public String[] getUsage() {
        return usage;
    }
    
    
    public Set<String> getAliases() {
        
    }
}
