package net.zetaeta.libraries.commands.local;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.zetaeta.libraries.ZPUtil;
import net.zetaeta.libraries.commands.CommandArguments;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecutorWrapper extends AbstractLocalCommandExecutor {
    protected LocalCommandExecutor executor;
    protected Method executorMethod;
    protected boolean useCmdArgs;
    protected String[] boolFlags;
    protected String[] valueFlags;
    protected boolean checkPermissions;
    protected boolean playersOnly;
    protected String permission;
    
    public ExecutorWrapper(LocalCommand parent, LocalCommandExecutor executor, Method executorMethod) {
        super(parent);
        this.executor = executor;
        this.executorMethod = executorMethod;
        Command cmdAnnotation = executorMethod.getAnnotation(Command.class);
        usage = cmdAnnotation.usage();
        aliases = cmdAnnotation.aliases();
        if (aliases.length == 0) {
            aliases = new String[] {cmdAnnotation.value()};
        }
        useCmdArgs = cmdAnnotation.useCommandArguments();
        boolFlags = cmdAnnotation.boolFlags();
        valueFlags = cmdAnnotation.valueFlags();
        checkPermissions = cmdAnnotation.checkPermissions();
        playersOnly = cmdAnnotation.playersOnly();
        permission = cmdAnnotation.permission();
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        Bukkit.getLogger().info("ExecutorWrapper: execute");
        if (trySubCommand(sender, alias, args)) {
            return true;
        }
        if (checkPermissions && !ZPUtil.checkPermission(sender, permission, true, true)) {
            return true;
        }
        if (playersOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be run by a player!");
            return true;
        }
        boolean success;
        try {
            if (useCmdArgs) {
                success = (Boolean) executorMethod.invoke(executor, sender, CommandArguments.processArguments(args, boolFlags, valueFlags));
            }
            else {
                success = (Boolean) executorMethod.invoke(executor, sender, alias, args);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            sender.sendMessage("§cAn internal error occurred while executing this command.");
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            sender.sendMessage("§cAn internal error occurred while executing this command.");
            return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            e.getCause().printStackTrace();
            sender.sendMessage("§cAn internal error occurred while executing this command.");
            return true;
        }
        return success;
    }

    @Override
    public void sendUsage(CommandSender target) {
        target.sendMessage(usage);
    }
    
    public LocalPermission createPermission(String valueString) {
        return null;
    }
}
