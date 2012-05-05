package net.zetaeta.libraries.commands.local;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zetaeta.libraries.ZPUtil;

import org.bukkit.command.CommandSender;

/**
 * A class providing command management within a plugin based on the
 * Settlement model. 
 * 
 * @author Zetaeta
 */
public abstract class AbstractLocalCommandExecutor implements LocalCommand {
    protected Map<String, LocalCommand> subCommands;
    protected String[] usage;
    protected String[] aliases;
    protected LocalPermission permission;
    protected LocalCommand parent;
    
    /**
     * Creates an empty {@link AbstractLocalCommandExecutor}, only initialising the subCommands Map and aliases Set.
     */
    public AbstractLocalCommandExecutor() {
        subCommands = new HashMap<String, LocalCommand>();
        aliases = new String[0];
    }
    
    /**
     * Creates a bare AbstractLocalCommandExecutor with specified parent.
     * 
     * @param parent Parent LocalCommand for this command.
     */
    public AbstractLocalCommandExecutor(LocalCommand parent) {
        this();
        this.parent = parent;
    }
    
    /**
     * Creates an AbstractCommandExecutor with specified arguments, useful for having small anonymous classes as LocalCommandExecutors.
     * 
     * @param parent Parent LocalCommand
     * @param permission Permission for this command.
     * @param usage Usage message when the command fails.
     * @param aliases Aliases of the command.
     */
    public AbstractLocalCommandExecutor(LocalCommand parent, LocalPermission permission, String[] usage, String[] aliases) {
        this.usage = usage;
        this.parent = parent;
        this.permission = permission;
        this.aliases = aliases;
        subCommands = new HashMap<String, LocalCommand>();
    }
    
    /**
     * {@inheritDoc}
     */
    public LocalCommand getParent() {
        return parent;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Collection<LocalCommand> getSubCommands() {
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
    public LocalCommand registerSubCommand(LocalCommand subCommandExecutor) {
        String[] subAliases = subCommandExecutor.getAliases();
        for (String alias : subAliases) {
            registerSubCommand(alias, subCommandExecutor);
        }
        return subCommandExecutor;
    }
    
    
    public List<LocalCommand> registerSubCommands(LocalCommandExecutor commandsExecutor) {
        Class<? extends LocalCommandExecutor> executorClass = commandsExecutor.getClass();
        List<LocalCommand> registered = new ArrayList<LocalCommand>(executorClass.getMethods().length);
        for (Method m : executorClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Command.class)) {
                registered.add(registerSubCommand(new ExecutorWrapper(this, commandsExecutor, m)));
            }
        }
        return registered;
    }
    
    /**
     * Used to register a subcommand executor to this command.
     * Even if a command should not have any subcommands, it should still be properly implemented for future convenience or to make extensions easier.
     * 
     * @param subCommand Subcommand alias to register.
     * @param executor AbstractLocalCommandExecutor to register for to be registered.
     */
    public void registerSubCommand(String subCommand, LocalCommand executor) {
        subCommands.put(subCommand, executor);
    }

    /**
     * Finds and executes any required subcommands, sending the sender the command's usage if the subcommand returns true.
     * 
     * @param sender Sender of the command to pass to the subcommand
     * @param alias Current command's alias.
     * @param args Arguments of the current command.
     * @return True if a subcommand is run, false otherwise. It is recommended for the current command to return true if this method returns true.
     */
    @SuppressWarnings("static-access")
    public boolean trySubCommand(CommandSender sender, String alias, String[] args) {
        
        if (args.length < 1) {
            return false;
        }
        if (subCommands.containsKey(args[0])) {
            if (subCommands.get(args[0]).execute(sender, args[0], ZPUtil.removeFirstIndex(args))) {
                return true;
            }
            else {
//                SettlementMessenger.sendUsage(sender, subCommands.get(args[0]).getUsage());
                sendUsage(sender);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public abstract boolean execute(CommandSender sender, String alias, String[] args);
    
    public abstract void sendUsage(CommandSender target);
}
