package net.zetaeta.plugins.libraries.commands.management;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandsManager {
    private JavaPlugin plugin;
    private Logger pluginLogger;
    private CommandMap commandMap;
    
    public CommandsManager(JavaPlugin plugin) {
        pluginLogger = plugin.getLogger();
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandExecutor that will be executing the command. 
     * If the executor extends PluginCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.plugins.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @param permission Permission of the command, if you want the command itself to do permission checking.
     * @param permissionMessage Message to be shown if player does not have the permission. Set to null for the default Bukkit error.
     * */
    public boolean registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description, String permission, String permissionMessage) {
        DynamicPluginCommand command = new DynamicPluginCommand(commandName, plugin);
        command.setAliases(Arrays.asList(aliases)).setUsage(usage).setDescription(description);
        command.setPermission(permission);
        command.setPermissionMessage(permissionMessage);
        
        PluginManager pm = Bukkit.getPluginManager();
        if (pm instanceof SimplePluginManager) {
            Class<SimplePluginManager> spmClass = SimplePluginManager.class;
            try {
                Field field = spmClass.getField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(pm);
                commandMap.register(plugin.getDescription().getName(), command);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    public boolean registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description, String permission) {
        return registerCommand(commandName, executor, aliases, usage, description, permission, "§cYou do not have access to that command!");
    }
    
    public boolean registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description) {
        return registerCommand(commandName, executor, aliases, usage, description, null);
    }
    
    public boolean registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage) {
        return registerCommand(commandName, executor, aliases, usage, "");
    }
    
    public boolean registerCommand(String commandName, CommandExecutor executor, String[] aliases) {
        return registerCommand(commandName, executor, aliases, "/<command>");
    }
    
    public boolean registerCommand(String commandName, CommandExecutor executor, String usage) {
        return registerCommand(commandName, executor, new String[] {}, usage);
    }
    
    public boolean registerCommand(String commandName, CommandExecutor executor) {
        return registerCommand(commandName, executor, new String[] {}, "/<command>");
    }
    
    
    public boolean registerCommand(String commandName, String[] aliases, String usage, String description, String permission, String permissionMessage) {
        return registerCommand(commandName, plugin, aliases, usage, description, permission, permissionMessage);
    }
    
    public boolean registerCommand(String commandName,  String[] aliases, String usage, String description, String permission) {
        return registerCommand(commandName, plugin, aliases, usage, description, permission);
    }
    
    public boolean registerCommand(String commandName, String[] aliases, String usage, String description) {
        return registerCommand(commandName, plugin, aliases, usage, description);
    }
    
    public boolean registerCommand(String commandName, String[] aliases, String usage) {
        return registerCommand(commandName, plugin, aliases, usage);
    }
    
    public boolean registerCommand(String commandName, String[] aliases) {
        return registerCommand(commandName, plugin, aliases, "/<command>");
    }
    
    public boolean registerCommand(String commandName, String usage) {
        return registerCommand(commandName, plugin, new String[] {}, usage);
    }
    
    public boolean registerCommand(String commandName) {
        return registerCommand(commandName, plugin, new String[] {}, "/<command>");
    }
    
    
    public boolean unregisterCommand(String commandName) {
        
    }
}
