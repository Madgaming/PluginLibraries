package net.zetaeta.libraries;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides utility methods for my plugins.
 * 
 * @author Zetaeta
 *
 */
public class ZPUtil {
    
    
    /**
     * Checks whether a CommandSender has a permission, telling them they do not have acces to the command if they don't have it.
     * 
     * @param sender CommandSender to test
     * @param permission String permission to test
     * @return Whether sender has permission
     */
    public static final boolean checkPermission(CommandSender sender, String permission) {
        if(sender.hasPermission(permission)) return true;
        sender.sendMessage("§cYou do not have access to that command!");
        return false;
    }
    
    
    /**
     * Adds the basic colours (0 - f) to a String by replacing "&<colour digit>" with §<colour digit>
     * 
     * @param string String to colourise
     * @return Colourised string.
     */
    public static final String addBasicColour(String string) {
        string = string.replaceAll("(§([a-fA-F0-9]))", "§$2");

        string = string.replaceAll("(&([a-fA-F0-9]))", "§$2");

        return string;
      }
    
    @SuppressWarnings("javadoc")
    public static final FileConfiguration getFileConfiguration(JavaPlugin plugin, String filename, boolean load) throws IOException {
        File file = new File(plugin.getDataFolder(), filename);
        file.createNewFile();
        FileConfiguration newcfg = YamlConfiguration.loadConfiguration(file);
        if(load) {
            InputStream confDefStream = plugin.getResource(filename);
            YamlConfiguration confDef = YamlConfiguration.loadConfiguration(confDefStream);
            newcfg.setDefaults(confDef);
        }
        return newcfg;
    }
    
    
    /**
     * Concatenates an array of Strings into a single String by putting spaces between them
     * 
     * @param array Array of Strings to be concatenated
     * @return Single String representing the array
     */
    public static final String arrayAsString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s).append(" ");
        }
        return sb.toString();
    }
    
    
    /**
     * Removes the first index of an array of <T>. Convenience method for Arrays.copyOfRange(array, 1, array.length)
     * 
     * @param array Array of <T> to be modified
     * @return array without the first index.
     */
    public static final <T> T[] removeFirstIndex(T[] array) {
        if (array.length == 0) {
            return array;
        }
        return Arrays.copyOfRange(array, 1, array.length);
    }
    
    /**
     * Concatenates multiple objects into a String using StringBuilder and String.valueOf(Object)
     * 
     * @param args Objects to concat
     * @return String value of Objects concatenated.
     */
    public static final String concatString(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object o : args) {
            sb.append(String.valueOf(o));
        }
        return sb.toString();
    }
    
    /**
     * Concatenates multiple Strings into a single String using StringBuilder
     * 
     * @param args Strings to concat
     * @return String value of args concatenated.
     */
    public static final String concatString(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s);
        }
        return sb.toString();
    }
}
