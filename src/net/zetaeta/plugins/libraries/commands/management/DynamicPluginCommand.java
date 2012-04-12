package net.zetaeta.plugins.libraries.commands.management;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class DynamicPluginCommand extends Command {

    private CommandExecutor executor;
    private boolean isAnnotated = false;
    private Method executorMethod;
    private Plugin ownerPlugin;
    
    public DynamicPluginCommand(String name, String description,
            String usageMessage, List<String> aliases, Plugin plugin, CommandExecutor exec) {
        super(name, description, usageMessage, aliases);
        executor = exec;
        ownerPlugin = plugin;
        if (executor instanceof PluginCommandExecutor) {
            registerAnnotatedExecutor();
        }
    }

    public DynamicPluginCommand(String name, Plugin plugin, CommandExecutor exec) {
        this(name, "", "/" + name, new ArrayList<String>(), plugin, exec);
    }
    
    public DynamicPluginCommand(String name, Plugin plugin) {
        this(name, plugin, plugin);
    }
    
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
                if (annot.annotationType() == net.zetaeta.plugins.libraries.commands.Command.class) {
                    if (((net.zetaeta.plugins.libraries.commands.Command) annot).value().equalsIgnoreCase(this.getName())) {
                        if (m.getName().equals("onCommand")) {
                            isAnnotated = false;
                            executorMethod = null;
                            return;
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
