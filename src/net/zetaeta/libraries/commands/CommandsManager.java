package net.zetaeta.libraries.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zetaeta.libraries.util.ReflectionUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages commands for this library, with facilities for dynamically registering and unregistering commands.
 * 
 * @author Zetaeta
 */
public class CommandsManager {
    private JavaPlugin plugin;
    private CommandMap commandMap;
    
    /**
     * @param plugin JavaPlugin for which this instance is managing.
     */
    public CommandsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        PluginManager pm = Bukkit.getPluginManager();
        if (pm instanceof SimplePluginManager) {
            Class<SimplePluginManager> spmClass = SimplePluginManager.class;
            try {
                Field field = spmClass.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(pm);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @param permission Permission of the command, if you want the command itself to do permission checking.
     * @param permissionMessage Message to be shown if player does not have the permission. Set to null for the default Bukkit error.
     * @param executorMethod Method that will execute this command.
     * 
     * @return Command instance created by the manager.
     */
    public DynamicPluginCommand registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description, String permission, String permissionMessage, Method executorMethod) {
        DynamicPluginCommand cmd = registerCommand(commandName, executor, aliases, usage, description, permission, permissionMessage);
        cmd.setExecutorMethod(executorMethod);
        return cmd;
    }

    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @param permission Permission of the command, if you want the command itself to do permission checking.
     * @param permissionMessage Message to be shown if player does not have the permission. Set to null for the default Bukkit error.
     * 
     * @return Command instance created by the manager.
     */
    public DynamicPluginCommand registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description, String permission, String permissionMessage) {
        DynamicPluginCommand command = new DynamicPluginCommand(commandName, plugin, executor);
        if (aliases != null) {
            command.setAliases(Arrays.asList(aliases));
        }
        if (usage != null) {
            command.setUsage(usage);
        }
        if (description != null) {
            command.setDescription(description);
        }
        if (permission != null) {
            command.setPermission(permission);
        }
        if (permissionMessage != null) {
            command.setPermissionMessage(permissionMessage);
        }
        
        try {
            commandMap.register(plugin.getDescription().getName(), command);
            return command;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @param permission Permission of the command, if you want the command itself to do permission checking.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description, String permission) {
        return registerCommand(commandName, executor, aliases, usage, description, permission, "§cYou do not have access to that command!");
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage, String description) {
        return registerCommand(commandName, executor, aliases, usage, description, null);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, CommandExecutor executor, String[] aliases, String usage) {
        return registerCommand(commandName, executor, aliases, usage, "");
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param aliases Command's aliases.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, CommandExecutor executor, String[] aliases) {
        return registerCommand(commandName, executor, aliases, "/<command>");
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @param usage Usage message of command.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, CommandExecutor executor, String usage) {
        return registerCommand(commandName, executor, new String[] {}, usage);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param executor CommandManager that will be executing the command. 
     * If the executor extends DynamicCommandExecutor, the class will be checked for methods annotated with {@link net.zetaeta.libraries.commands.Command @Command}, 
     * and if there is a method with that annotation specifying this command it will be set as executor.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, CommandExecutor executor) {
        return registerCommand(commandName, executor, new String[] {}, "/<command>");
    }
    
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @param permission Permission of the command, if you want the command itself to do permission checking.
     * @param permissionMessage Message to be shown if player does not have the permission. Set to null for the default Bukkit error.
     * 
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, String[] aliases, String usage, String description, String permission, String permissionMessage) {
        return registerCommand(commandName, plugin, aliases, usage, description, permission, permissionMessage);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @param permission Permission of the command, if you want the command itself to do permission checking.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName,  String[] aliases, String usage, String description, String permission) {
        return registerCommand(commandName, plugin, aliases, usage, description, permission);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @param description Description of the command, used by some help plugins.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, String[] aliases, String usage, String description) {
        return registerCommand(commandName, plugin, aliases, usage, description);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param aliases Command's aliases.
     * @param usage Usage message of command.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, String[] aliases, String usage) {
        return registerCommand(commandName, plugin, aliases, usage);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param aliases Command's aliases.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, String[] aliases) {
        return registerCommand(commandName, plugin, aliases, "/<command>");
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @param usage Usage message of command.
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName, String usage) {
        return registerCommand(commandName, plugin, new String[] {}, usage);
    }
    
    /**
     * Registers a command to the CommandMap, for use registering commands dynamically after enabling.
     * 
     * @param commandName Main command, without the "/".
     * @return Command instance created by the manager.
     */
    public Command registerCommand(String commandName) {
        return registerCommand(commandName, plugin, new String[] {}, "/<command>");
    }
    
    /**
     * Registers all the ({@link net.zetaeta.libraries.commands.Command @Command}-annotated commands in the given CommandExecutor.
     * 
     * @param executor CommandExecutor to register Commands in.
     * @return List of command registered.
     */
    public List<Command> registerCommands(DynamicCommandExecutor executor) {
        Class<? extends DynamicCommandExecutor> clazz = executor.getClass();
        
        Method[] methods = clazz.getMethods();
        
        List<Command> registered = new ArrayList<Command>();
        
        for (Method m : methods) {
            if (m.isAnnotationPresent(net.zetaeta.libraries.commands.Command.class)) {
                net.zetaeta.libraries.commands.Command cmdAnnot = m.getAnnotation(net.zetaeta.libraries.commands.Command.class);
                String name = cmdAnnot.value();
                String[] aliases = cmdAnnot.aliases();
                String description = cmdAnnot.description();
                String usage = cmdAnnot.usage();
                String perm = cmdAnnot.permission();
                String permMessage = cmdAnnot.permissionMessage();
                try {
                    Command cmd = registerCommand(name, executor, aliases, usage, description, perm, permMessage, m);
                    commandMap.register(plugin.getName(), cmd);
                    registered.add(cmd);
                } catch (Throwable e) {
                    plugin.getLogger().severe("Could not register command " + name + "!");
                    e.printStackTrace();
                }
            }
        }
        return registered;
    }
    
    /**
     * 
     * @param commandName Name of the command to unregister.
     * @return true if command was successfully unregistered, false if an error occurred or the command was not registered.
     */
    public boolean unregisterCommand(String commandName) {
        SimpleCommandMap scm = (SimpleCommandMap) commandMap;
        try {
            Map<String, Command> knownCommands = ReflectionUtil.getField(scm, "knownCommands");
            Set<String> aliases = ReflectionUtil.getField(scm, "aliases");
            Command cmd = knownCommands.get(commandName);
            if (cmd == null) {
                return false;
            }
            List<String> cmdAliases = cmd.getAliases();
            for (String al : cmdAliases) {
                aliases.remove(al);
                knownCommands.remove(al);
            }
            cmd.unregister(scm);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets the command with the specific label.
     * 
     * @param name Name of the command.
     * @return Command represented by the name.
     */
    public Command getCommand(String name) {
        return commandMap.getCommand(name);
    }
    
    /**
     * Gets the command with the specific label.
     * 
     * @param name Name of the command
     * @param includeAliases Whether to include aliases in the search for the command.
     * @return The command with the specified name (or aliases if includeAliases = true)
     */
    public Command getCommand(String name, boolean includeAliases) {
        Command cmd = commandMap.getCommand(name);
        if (includeAliases) {
            return cmd;
        }
        else {
            if (cmd.getLabel().equalsIgnoreCase(name) || cmd.getName().equalsIgnoreCase(name)) {
                return cmd;
            }
            return null;
        }
    }
}
