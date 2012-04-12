package net.zetaeta.plugins.libraries;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ZPUtil {
	public static boolean checkPermission(CommandSender sndr, String perm) {
		if(sndr.hasPermission(perm)) return true;
		sndr.sendMessage("§cYou do not have access to that command!");
		return false;
	}
	
	public static String addBasicColour(String string) {
	    string = string.replaceAll("(§([a-fA-F0-9]))", "§$2");

	    string = string.replaceAll("(&([a-fA-F0-9]))", "§$2");

	    return string;
	  }
	
	public static FileConfiguration getFileConfiguration(JavaPlugin plugin, String filename, boolean load) throws IOException {
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
	
	public static String arrayAsString(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (String s : array) {
			sb.append(s).append(" ");
		}
		return sb.toString();
	}
	
	public static <T> T[] removeFirstIndex(T[] array) {
	    return Arrays.copyOfRange(array, 0, array.length);
	}
}
