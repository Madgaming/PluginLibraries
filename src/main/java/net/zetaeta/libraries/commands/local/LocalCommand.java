package net.zetaeta.libraries.commands.local;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

/**
 * @author Zetaeta
 *
 */
public interface LocalCommand {

    /**
     * Gets the parent LocalCommand in the command tree.
     *
     * @return parent LocalCommand
     */
    public LocalCommand getParent();

    /**
     * Gets the registered subcommands' executors
     *
     * @return Set of LocalCommandExecutors registered as subcommands.
     */
    public Collection<LocalCommand> getSubCommands();
    
    /**
     * Gets the registered subcommands' executors
     *
     * @return Set of LocalCommandExecutors registered as subcommands.
     */
    public Collection<LocalCommand> getOrderedSubCommands();
    
    /**
     * @return aliases of all registered subcommands.
     */
    public Set<String> getSubCommandAliases();

    /**
     * Gets the LocalPermission for this command.
     *
     * @return LocalPermission required for this command.
     */
    public String getPermission();

    /**
     * Gets the usage list for the command, used for improperly formed command or {@literal /settlement help <command>}
     *
     * @return Command's usage info, in String[] form ready for {@link org.bukkit.command.CommandSender#sendMessage(String[]) sendMessage()}.
     */
    public String[] getUsage();
    
    /**
     * Gets a short usage message for the command, for use in /settlement help and similar.
     * 
     * @return Short usage message.
     */
    public String[] getShortUsage();

    /**
     * Gets different possible aliases for the subcommand.
     *
     * @return Aliases of the command.
     */
    public String[] getAliases();

    /**
     * Used to register a subcommand executor to this command.
     * Even if a command should not have any subcommands, it should still be properly implemented
     * for future convenience and to make extensions easier.
     *
     * @param subCommandExecutor Subcommand to be registered.
     * @return Registered subcommand for simplicity.
     */
    public LocalCommand registerSubCommand(LocalCommand subCommandExecutor);
    
    /**
     * Registers a LocalCommandExecutor that uses individual methods instead of classes to execute commands.
     * 
     * @param commandsExecutor
     * @return {@link List} of registered commands.
     */
    public List<LocalCommand> registerSubCommands(LocalCommandExecutor commandsExecutor);
    
    /**
     * Executes the subcommand.
     * 
     * @param sender Sender of subcommand
     * @param alias of the command used.
     * @param args Arguments to the command.
     * @return Whether subcommand executed successfully.
     */
    public boolean execute(CommandSender sender, String alias, String[] args);
    
    /**
     * Sends the command's usage message to the target, using the plugin's preferred means of sending it.
     * 
     * @param target CommandSender to send the usage message to.
     */
    public void sendUsage(CommandSender target);
    
    /**
     * Gets the LocalCommand representing the given subcommand alias.
     *
     * @param alias Alias of a subcommand to get the executor of.
     * @return alias' LocalCommand, or null if no subcommand of that name exists.
     */
    public LocalCommand getSubCommand(String alias);
    
    /**
     * Gets the LocalCommand representing the given subcommand aliases.
     *
     * @param aliases Alias of a subcommand to recursively get the executor of.
     * @return aliases' LocalCommand, or null if no subcommand of that name exists.
     */
    public LocalCommand getSubCommand(String[] aliases);
}
