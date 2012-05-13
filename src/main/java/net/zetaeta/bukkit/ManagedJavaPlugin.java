package net.zetaeta.bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.zetaeta.bukkit.commands.CommandsManager;
import net.zetaeta.bukkit.configuration.PluginConfiguration;
import net.zetaeta.bukkit.util.FileUtil;

import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Zetaeta
 * 
 * A JavaPlugin with some predefined utility methods, useful (but not required) for using a CommandsManager.4
 * Not particularly useful now, may add more stuff in future.
 *
 */
@Useless
public abstract class ManagedJavaPlugin extends JavaPlugin {
    private File configFile;
    private PluginConfiguration config;
    private Logger log;
    
    /**
     * Represents the CommandsManager for this plugin.
     */
    protected CommandsManager commandsManager;
    
    /**
     * @param name Name of the command to get
     * @return Command with label <code>name</code>.
     */
    public Command getGenericCommand(String name) {
        return commandsManager.getCommand(name, false);
    }
    
    @Override
    public PluginConfiguration getConfig() {
        log = getLogger();
        log.info("getConfig");
        if (config == null) {
            log.info("CONFIG == NULL");
            reloadConfig();
        }
        log.info("getConfig: keys: " + config.getKeys(true));
        return config;
    }
    
    @Override
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        log.info("reloadConfig");
        getDataFolder().mkdirs();
        try {
            configFile.createNewFile();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not create config file!", e);
            e.printStackTrace();
        }
        System.out.println("Created files!");
        System.out.println("File length = " + configFile.length());
        System.out.println("File location = " + configFile.getAbsolutePath());
        if (configFile.length() == 0 && getResource("config.yml") != null) {
            System.out.println("Copying defaults!");
            try {
                FileUtil.copyStreams(getResource("config.yml"), new FileOutputStream(configFile));
            } catch (FileNotFoundException e) {
                getLogger().log(Level.SEVERE, "Could not save defaults to config file", e);
                e.printStackTrace();
            }
        }
        config = PluginConfiguration.loadConfiguration(configFile);
        InputStream defaults = getResource("config.yml");
        if (defaults != null) {
            log.info("########IGNORE BELOW########");
            config.setDefaults(PluginConfiguration.loadConfiguration(defaults));
            log.info("########IGNORE ABOVE########");
        }
    }
    
    @Override
    public void saveConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }
}
