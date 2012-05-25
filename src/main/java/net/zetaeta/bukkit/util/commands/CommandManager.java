package net.zetaeta.bukkit.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.zetaeta.bukkit.ZetaPlugin;
import net.zetaeta.bukkit.util.PermissionUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@Deprecated
public class CommandManager {
	
	static Map<String, ExecutorMethod> executors = new HashMap<String, ExecutorMethod>();
	
	public ZetaPlugin plugin;
	Logger log;
	
	public CommandManager(ZetaPlugin plugin) {
		this.plugin = plugin;
		log = plugin.getPluginLogger();
	}
	
	
	
	@SuppressWarnings("boxing")
	public boolean onCommand(CommandSender sndr, Command cmd, String cmdlbl, String[] args) {
		if(!executors.containsKey(cmd.getName())) {
			sndr.sendMessage("§cINTERNAL ERROR: CommandHandler executor class not found. Please report this to your Administrator.");
			log.log(Level.SEVERE, "INTERNAL ERROR: CommandHandler executor class for " + cmd.getName() + " not found!");
			return true;
		}
		Method commandMethod = executors.get(cmd.getName()).method;
		Executor executor = executors.get(cmd.getName()).executor;
		String permission = executors.get(cmd.getName()).permission;
		if(!permission.equals("")) {
			if(!PermissionUtil.checkPermission(sndr, permission)) {
				return true;
			}
		}
		if(!commandMethod.isAccessible()) {
			
			log.log(Level.WARNING, "INTERNAL ERROR: CommandHandler executor method for " + cmd.getName() + " inaccessible!");
			try {
				commandMethod.setAccessible(true);
			} catch (Throwable ex) {
				log.info("ERROR: " + ex + " " + ex.getMessage());
				sndr.sendMessage("ERROR: " + ex + " " + ex.getMessage());
				return true;
			}
			
		}
		Class<?>[] params = commandMethod.getParameterTypes();
		
		if (params.length != 2) {
			sndr.sendMessage("§cINTERNAL ERROR: CommandHandler executor method has wrong number of arguments. Please report this to your Administrator.");
			log.log(Level.SEVERE, "INTERNAL ERROR: CommandHandler executor method for " + cmd.getName() + " has wrong number of arguments!");
			return true;
		}
		
		if(params[0] != CommandSender.class || params[1] != String[].class) {
			sndr.sendMessage("§cINTERNAL ERROR: CommandHandler executor method has improper arguments. Please report this to your Administrator.");
			log.log(Level.SEVERE, "INTERNAL ERROR: CommandHandler executor method for " + cmd.getName() + " has improper arguments!");
			return true;
		}
		
		try {
			Boolean returns = (Boolean) commandMethod.invoke(executor, (Object)sndr, (Object[])args);
			return returns;
		} catch (IllegalAccessException e) {
			sndr.sendMessage("§cINTERNAL ERROR: CommandHandler executor method inaccessible. Please report this to your Administrator.");
			plugin.log.log(Level.SEVERE, "INTERNAL ERROR: CommandHandler executor method for " + cmd.getName() + " inaccessible!");
			return true;
		} catch (IllegalArgumentException e) {
			sndr.sendMessage("§cINTERNAL ERROR: CommandHandler executor method failed calling. Please report this to your Administrator.");
			plugin.log.log(Level.SEVERE, "INTERNAL ERROR: CommandHandler executor method for " + cmd.getName() + " failed calling!");
			return true;
		} catch (InvocationTargetException e) {
			sndr.sendMessage("§cINTERNAL ERROR: CommandHandler executor method failed calling. Please report this to your Administrator.");
			plugin.log.log(Level.SEVERE, "INTERNAL ERROR: CommandHandler executor method for " + cmd.getName() + " failed calling!");
			return true;
		}
		
	}
	
	class ExecutorMethod {
		private Method method;
		private Executor executor;
		private String permission = "";
		public ExecutorMethod(Executor e, Method m) {
			method = m;
			executor = e;
		}
		public ExecutorMethod(Executor e, Method m, String perm) {
			method = m;
			executor = e;
			permission = perm;
		}
	}
	
	public void registerCommandExecutor(Executor executor) {
		Method[] executorMethods = executor.getClass().getMethods();
		for(Method m : executorMethods) {
			if(m.isAnnotationPresent(net.zetaeta.bukkit.commands.CommandHandler.class)) {
				net.zetaeta.bukkit.commands.CommandHandler annot = m.getAnnotation(net.zetaeta.bukkit.commands.CommandHandler.class);
				if(annot.permission().equals("")) {
					executors.put(annot.value(), new ExecutorMethod(executor, m));
				} else {
					executors.put(annot.value(), new ExecutorMethod(executor, m, annot.permission()));
				}
			}
		}
	}
}
