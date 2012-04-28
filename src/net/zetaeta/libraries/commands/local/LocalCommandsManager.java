package net.zetaeta.libraries.commands.local;

import net.zetaeta.libraries.Useless;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Zetaeta
 *
 */
@Useless("Not needed here, should be implemented by users")
@Deprecated
public class LocalCommandsManager extends AbstractLocalCommandExecutor {
    public static LocalCommandsManager localCommandsManager;
    protected JavaPlugin plugin;
    
    public LocalCommandsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        localCommandsManager = this;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        // TODO Auto-generated method stub
        return false;
    }
}
