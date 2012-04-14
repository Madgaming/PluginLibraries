package net.zetaeta.libraries.commands.local;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Zetaeta
 *
 */
public class LocalCommandsManager extends LocalCommandExecutor {
    public static LocalCommandsManager localCommandsManager;
    protected JavaPlugin plugin;
    
    public LocalCommandsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        localCommandsManager = this;
    }
}
