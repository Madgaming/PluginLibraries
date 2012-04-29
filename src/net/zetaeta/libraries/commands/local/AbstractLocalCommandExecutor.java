package net.zetaeta.libraries.commands.local;

import java.util.Collection;
import java.util.HashMap;
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
    protected String[] aliases;
    protected LocalPermission permission;
    protected LocalCommandExecutor parent;
    
    /**
     * Creates an empty {@link AbstractLocalCommandExecutor},only initialising the subCommands Map and aliases Set.
     */
    public AbstractLocalCommandExecutor() {
        subCommands = new HashMap<String, LocalCommandExecutor>();
        aliases = new String[0];
    }
    
    /**
     * Creates a bare AbstractLocalCommandExecutor with specified parent.
     * 
     * @param parent Parent LocalCommandExecutor for this command.
     */
    public AbstractLocalCommandExecutor(LocalCommandExecutor parent) {
        this();
        this.parent = parent;
    }
    
    /**
     * Creates an AbstractCommandExecutor with specified arguments, useful for having small anonymous classes as LocalCommandExecutors.
     * 
     * @param parent Parent LocalCommandExecutor
     * @param permission Permission for this command.
     * @param usage Usage message when the command fails.
     * @param aliases Aliases of the command.
     */
    public AbstractLocalCommandExecutor(LocalCommandExecutor parent, LocalPermission permission, String[] usage, String[] aliases) {
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
    public Collection<LocalCommandExecutor> getSubCommands() {
        return subCommands.values();
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<String> getSubCommandAliases() {
        return subCommands.keySet();
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
    public String[] getAliases() {
        return aliases;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void registerSubCommand(LocalCommandExecutor subCommandExecutor) {
        String[] subAliases = subCommandExecutor.getAliases();
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
