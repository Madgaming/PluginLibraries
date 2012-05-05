package net.zetaeta.libraries;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel
 *
 */
@Deprecated
public abstract class ZetaPlugin extends JavaPlugin {
	public Logger log;
	
	public abstract Logger getPluginLogger();
}
