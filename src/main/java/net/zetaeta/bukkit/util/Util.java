package net.zetaeta.bukkit.util;

import static org.bukkit.Bukkit.getPluginManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides utility methods for my plugins.
 * 
 * @author Zetaeta
 *
 */
public class Util {
    
    /**
     * Adds the basic colours (0 - f) to a String by replacing "&<colour digit>" with §<colour digit>
     * 
     * @param string String to colourise
     * @return Colourised string.
     */
    public static String addBasicColour(String string) {
        string = string.replaceAll("(§([a-fA-F0-9]))", "§$2");

        string = string.replaceAll("(&([a-fA-F0-9]))", "§$2");

        return string;
      }
    
    /**
     * Removes the first index of an array of <T>. Convenience method for Arrays.copyOfRange(array, 1, array.length)
     * 
     * @param array Array of <T> to be modified
     * @return array without the first index.
     */
    public static <T> T[] removeFirstIndex(T[] array) {
        if (array.length == 0) {
            return array;
        }
        return Arrays.copyOfRange(array, 1, array.length);
    }
    
    public static boolean booleanValue(int i) {
        return i == 0 ? false : true;
    }
    
    public static boolean booleanValue(long l) {
        return l == 0 ? false : true;
    }
    
    public static boolean booleanValue(short s) {
        return s == 0 ? false : true;
    }
    
    public static boolean booleanValue(byte b) {
        return b == 0 ? false : true;
    }
}
