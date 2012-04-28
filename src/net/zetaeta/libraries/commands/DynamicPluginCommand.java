package net.zetaeta.libraries.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Dynamically registered command used by the CommandsManager.
 * 
 * @author Zetaeta
 *
 */
public class DynamicPluginCommand extends Command {

    private CommandExecutor executor;
    private boolean isAnnotated = false;
    private Method executorMethod;
    private Plugin ownerPlugin;
    
    /**
     * 
     * @param name Command name.
     * @param description Command description, useful for help plugins and suchlike.
     * @param usageMessage Message returned when command fails.
     * @param aliases Aliases of the command
     * @param plugin Owner plugin of the command
     * @param exec CommandExecutor of the command.
     */
    public DynamicPluginCommand(String name, String description,
            String usageMessage, List<String> aliases, Plugin plugin, CommandExecutor exec) {
        super(name, description, usageMessage, aliases);
        executor = exec;
        ownerPlugin = plugin;
        if (executor instanceof DynamicCommandExecutor) {
            registerAnnotatedExecutor();
        }
    }
    
    /**
     * 
     * @param name Command name.
     * @param description Command description, useful for help plugins and suchlike.
     * @param usageMessage Message returned when command fails.
     * @param aliases Aliases of the command
     * @param plugin Owner plugin of the command
     * @param executor CommandExecutor of the command.
     * @param executorMethod Method that handles execution of the command.
     */
    public DynamicPluginCommand(String name, String description, String usageMessage, 
            List<String> aliases, Plugin plugin, CommandExecutor executor, Method executorMethod) {
        super(name, description, usageMessage, aliases);
        this.executor = executor;
        ownerPlugin = plugin;
        isAnnotated = true;
        this.executorMethod = executorMethod;
    }

    /**
     * 
     * @param name Command name.
     * @param plugin Owner plugin of the command
     * @param exec CommandExecutor of the command.
     */
    public DynamicPluginCommand(String name, Plugin plugin, CommandExecutor exec) {
        this(name, "", "/" + name, new ArrayList<String>(), plugin, exec);
    }
    
    /**
     * 
     * @param name Command name.
     * @param plugin Owner plugin of the command
     */
    public DynamicPluginCommand(String name, Plugin plugin) {
        this(name, plugin, plugin);
    }
    
    /**
     * 
     * @param name Command name.
     * @param description Command description, useful for help plugins and suchlike.
     * @param usageMessage Message returned when command fails.
     * @param aliases Aliases of the command
     * @param plugin Owner plugin of the command
     */
    public DynamicPluginCommand(String name, String description, String usageMessage, List<String> aliases, Plugin plugin) {
        this(name, description, usageMessage, aliases, plugin, plugin);
    }

    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        
        if (!ownerPlugin.isEnabled()) {
            return false;
        }
        
        if (!testPermission(sender)) {
            return true;
        }
        
        boolean success = false;
        try {
            if (!isAnnotated) {
                success = executor.onCommand(sender, this, commandLabel, args);
            }
            else {
                if (executorMethod == null) {
                    sender.sendMessage(getUsage());
                    return false;
                }
                success = (Boolean) executorMethod.invoke(executor, sender, this, commandLabel, args);
            }
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + ownerPlugin.getDescription().getFullName(), ex);
        }
        
        if (!success) {
            sender.sendMessage(getUsage());
            return false;
        }
        return true;
        
    }
    
    
    private void registerAnnotatedExecutor() {
        Method[] methods = executor.getClass().getDeclaredMethods();
        for (Method m : methods) {
            Annotation[] annots = m.getAnnotations();
            for (Annotation annot : annots) {
                if (annot.annotationType() == net.zetaeta.libraries.commands.Command.class) {
                    net.zetaeta.libraries.commands.Command cmdAnnot = (net.zetaeta.libraries.commands.Command) annot;
                    if (cmdAnnot.value().equalsIgnoreCase(this.getName())) {
                        if (m.getName().equals("onCommand")) {
                            isAnnotated = false;
                            executorMethod = null;
                            return;
                        }
                        if (cmdAnnot.aliases().length > 0) {
                            this.setAliases(Arrays.asList(cmdAnnot.aliases()));
                        }
                        if (!cmdAnnot.description().equals("")) {
                            description = cmdAnnot.description();
                        }
                        isAnnotated = true;
                        executorMethod = m;
                        return;
                    }
                }
            }
        }
    }

}
