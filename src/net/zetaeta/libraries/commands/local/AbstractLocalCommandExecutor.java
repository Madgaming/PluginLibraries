package net.zetaeta.libraries.commands.local;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;

/**
 * A class providing command management within a plugin based on the
 * Settlement model. 
 * 
 * @author Zetaeta
 */
public abstract class AbstractLocalCommandExecutor implements LocalCommandExecutor {
    protected Map<String, LocalCommandExecutor> subCommands;
    protected String[] usage;
    protected Set<String> aliases;
    protected LocalPermission permission;
    protected LocalCommandExecutor parent;
    
    public AbstractLocalCommandExecutor() {
        subCommands = new HashMap<String, LocalCommandExecutor>();
        aliases = new HashSet<String>();
    }
    
    /**
     * Creates an AbstractCommandExecutor with specified arguments, useful for having small anonymous classes as LocalCommandExecutors.
     * 
     * @param parent Parent LocalCommandExecutor
     * @param permission Permission for this command.
     * @param usage Usage message when the command fails.
     * @param aliases Aliases of the command.
     */
    public AbstractLocalCommandExecutor(LocalCommandExecutor parent, LocalPermission permission, String[] usage, Set<String> aliases) {
        this.usage = usage;
        this.parent = parent;
        this.permission = permission;
        this.aliases = aliases;
        subCommands = new HashMap<String, LocalCommandExecutor>();
    }
    
    /**
     * {@inheritDoc}
     */
    public LocalCommandExecutor getParent() {
        return parent;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Set<LocalCommandExecutor> getSubCommands() {
        return new HashSet<LocalCommandExecutor>(subCommands.values());
    }
    
    
    /**
     * {@inheritDoc}
     */
    public LocalPermission getPermission() {
        return permission;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String[] getUsage() {
        return usage;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Set<String> getAliases() {
        return aliases;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void registerSubCommand(LocalCommandExecutor subCommandExecutor) {
        Set<String> subAliases = subCommandExecutor.getAliases();
        for (String alias : subAliases) {
            registerSubCommand(alias, subCommandExecutor);
        }
    }
    
    
    /**
     * Used to register a subcommand executor to this command.
     * Even if a command should not have any subcommands, it should still be properly implemented for future convenience or to make extensions easier.
     * 
     * @param subCommand Subcommand alias to register.
     * @param executor AbstractLocalCommandExecutor to register for to be registered.
     */
    public void registerSubCommand(String subCommand, LocalCommandExecutor executor) {
        subCommands.put(subCommand, executor);
    }


    /**
     * {@inheritDoc}
     */
    public abstract boolean execute(CommandSender sender, String alias, String[] args);
    
    
}
