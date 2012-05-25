package net.zetaeta.bukkit.util;

import static org.bukkit.Bukkit.getPluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class PermissionUtil {
    
    /**
     * Checks whether a CommandSender has a permission, telling them they do not have acces to the command if they don't have it.
     * 
     * @param sender CommandSender to test
     * @param permission String permission to test
     * @return Whether sender has permission
     */
    public static boolean checkPermission(CommandSender sender, String permission) {
        return checkPermission(sender, permission, true, true);
    }
    

    public static boolean checkPermission(CommandSender sender, String permission, boolean warn, boolean recursive) {
        boolean hasPerm;
        if (recursive) {
            hasPerm = checkPermissionRecursive(sender, permission);
        }
        else {
            hasPerm = sender.hasPermission(permission);
        }
        if (!hasPerm && warn) {
            sender.sendMessage("§cYou do not have access to that command!");
        }
        return hasPerm;
    }
    
    
    public static boolean checkPermissionRecursive(CommandSender sender, String permission) {
        String[] nodes = permission.split("\\.");
        if (nodes.length == 0) {
            return sender.hasPermission(permission);
        }
        if (getPluginManager().getPermission(permission) != null) {
            return sender.hasPermission(permission);
        }
        StringBuilder nodesIt = new StringBuilder(permission);
        String permIt = permission;
        List<String> toRegister = new ArrayList<String>(nodes.length);
        toRegister.add(permission);
        for (int i=nodes.length - 1, length = permission.length(); i > 0 && getPluginManager().getPermission(permIt) == null; --i) {
            nodesIt.delete(length - nodes[i].length(), length);
            length -= nodes[i].length();
            if (nodesIt.charAt(nodesIt.length() - 1) == '.') {
                nodesIt.deleteCharAt(nodesIt.length() - 1);
                --length;
            }
            permIt = nodesIt.toString().concat(".*");
            toRegister.add(permIt);
        }
        if (getPluginManager().getPermission(permIt) == null) {
            getPluginManager().addPermission(new Permission(permIt));
        }
        Permission parent = getPluginManager().getPermission(permIt), current = parent, previous = current;
        {
            for (ListIterator<String> registerIt = toRegister.listIterator(toRegister.size() - 1); registerIt.hasPrevious();) {
                String permStr = registerIt.previous();
                current = new Permission(permStr);
                current.addParent(previous, true);
                previous = current;
                if (!getPluginManager().getPermissions().contains(current)) {
                    getPluginManager().addPermission(current);
                }
            }
        }
        return sender.hasPermission(permission);
    }
    
}
