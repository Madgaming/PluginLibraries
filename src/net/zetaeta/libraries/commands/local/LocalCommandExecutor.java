package net.zetaeta.libraries.commands.local;

import java.util.Set;

import org.bukkit.command.CommandSender;

/**
 * @author Zetaeta
 *
 */
public interface LocalCommandExecutor {

    /**
     * Gets the parent LocalCommandExecutor in the command tree.
     *
     * @return parent LocalCommandExecutor
     */
    public LocalCommandExecutor getParent();

    /**
     * Gets the registered subcommands' executors
     *
     * @return Set of LocalCommandExecutors registered as subcommands.
     */
    public Set<LocalCommandExecutor> getSubCommands();

    /**
     * Gets the LocalPermission for this command.
     *
     * @return LocalPermission required for this command.
     */
    public LocalPermission getPermission();

    /**
     * Gets the usage list for the command, used for improperly formed command or {@literal /settlement help <command>}
     *
     * @return Command's usage info, in String[] form ready for {@link org.bukkit.command.CommandSender#sendMessage(String[]) sendMessage()}.
     */
    public String[] getUsage();

    /**
     * Gets different possible aliases for the subcommand.
     *
     * @return Aliases of the command.
     */
    public Set<String> getAliases();

    /**
     * Used to register a subcommand executor to this command.
     * Even if a command should not have any subcommands, it should still be properly implemented
     * for future convenience and to make extensions easier.
     *
     * @param subCommandExecutor Subcommand to be registered.
     */
    public void registerSubCommand(LocalCommandExecutor subCommandExecutor);
    
    /**
     * Executes the subcommand.
     * 
     * @param sender Sender of subcommand
     * @param command Subcommand sent.
     * @return Whether subcommand executed successfully.
     */
    public boolean execute(CommandSender sender, String commandName, String[] args);
}
