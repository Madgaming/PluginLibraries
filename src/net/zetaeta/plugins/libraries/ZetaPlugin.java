package net.zetaeta.plugins.libraries;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ZetaPlugin extends JavaPlugin {
	public Logger log;
	
	public abstract Logger getPluginLogger();
}
