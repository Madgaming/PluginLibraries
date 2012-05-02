package net.zetaeta.libraries.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zetaeta.libraries.Useless;
import net.zetaeta.libraries.ZPUtil;

import org.bukkit.command.CommandSender;

/**
 * @author Daniel
 *
 */
public class CommandArguments {
    private String[] args;
    private boolean processed;
    private Set<String> booleanFlags;
    private Map<String, String> valueFlagMap;
    private List<String> nonProcessedArguments;
    /**
     * Constructs a CommanArguments with the specified String array as arguments.
     * 
     * @param arguments String[] to process.
     */
    public CommandArguments(String[] arguments) {
        args = arguments;
        booleanFlags = new HashSet<String>();
        valueFlagMap = new HashMap<String, String>();
        nonProcessedArguments = new ArrayList<String>();
    }
    
    /**
     * Constructs a CommandArguments with the parameter String split by spaces.
     * Equivalent to {@link #CommandArguments(String[]) CommandArguments(arguments.split(" ")}.
     * 
     * @param arguments String to split and process.
     */
    public CommandArguments(String arguments) {
        this(arguments.split(" "));
    }
    
    /**
     * Constructs a CommandArguments with the parameter String split by the parameter split String.
     * Equivalent to {@link #CommandArguments(String[]) CommandArguments(arguments.split(split)}.
     * 
     * @param arguments String to split and process.
     * @param split Regex to split arguments by.
     */
    public CommandArguments(String arguments, String split) {
        this(arguments.split(split));
    }
    
    public static CommandArguments processArguments(String[] args, String[] boolFlags, String[] valueFlags) {
        CommandArguments cArgs = new CommandArguments(args);
        if (cArgs.process(Arrays.asList(boolFlags), Arrays.asList(valueFlags))) {
            return cArgs;
        }
        return null;
    }
    
    public static CommandArguments processArguments(String[] args, String[] boolFlags, String[] valueFlags, CommandSender errorReciever) {
        CommandArguments cArgs = processArguments(args, boolFlags, valueFlags);
        if (cArgs == null) {
            errorReciever.sendMessage("§cYour command " + ZPUtil.arrayAsString(args) + " could not be processed.");
        }
        return cArgs;
    }
    
    /**
     * Processes the CommandArguments
     *
     * @param boolFlags
     * @param valueFlags
     * @return True if parsing was successful, false otherwise. 
     */
    public boolean process(Collection<String> boolFlags, Collection<String> valueFlags) {
        int lastprocessed = -1;
        for (int i=0; i<args.length; ++i) {
            if (i <= lastprocessed) {
                continue;
            }
            if (args[i].startsWith("-")) {
                if (boolFlags.contains(args[i].substring(1))) {
                    booleanFlags.add(args[i].substring(1));
                }
                else if (valueFlags.contains(args[i].substring(1))) {
                    if (args.length == i + 1) {
                        return true;
                    }
                    if (args[i + 1].startsWith("\"")) {
                        if (args.length <= i + 2) {
                            continue;
                        }
                        StringBuilder value = new StringBuilder(args.length - i * 10);
                        value.append(args[i + 1]);
                        for (int j=i + 2; j<args.length; j++) {
                            if (args[j].endsWith("\"")) {
                                value.append(args[j].substring(0, args[j].length()));
                                lastprocessed = j;
                                break;
                            }
                            value.append(' ').append(args[j]);
                            lastprocessed = j;
                            if (j == args.length - 1) { // if this is the last index
                                return false;
                            }
                        }
                        valueFlagMap.put(args[i], value.toString());
                    }
                    else if (args[i + 1].startsWith("'")) {
                        if (args[i + 1].endsWith("\"")) {
                            valueFlagMap.put(args[i], args[i + 1].substring(1, args[i + 1].length() - 1));
                            lastprocessed = i + 1;
                            continue;
                        }
                        if (args.length <= i + 2) {
                            continue;
                        }
                        StringBuilder value = new StringBuilder(args.length - i * 10);
                        value.append(args[i + 1]);
                        for (int j=i + 2; j<args.length; j++) {
                            if (args[j].endsWith("'")) {
                                value.append(args[j].substring(0, args[j].length()));
                                lastprocessed = j;
                                break;
                            }
                            value.append(' ').append(args[j]);
                            lastprocessed = j;
                            if (j == args.length - 1) { // if this is the last index
                                return false;
                            }
                        }
                        valueFlagMap.put(args[i], value.toString());
                    }
                    else {
                        valueFlagMap.put(args[i], args[i + 1]);
                        lastprocessed = i + 1;
                    }
                }
            }
            else {
                nonProcessedArguments.add(args[i]);
            }
        }
        processed = true;
        return true;
        
    }
    
    public Set<String> getBooleanFlags() {
        return booleanFlags;
    }
    
    public Map<String, String> getValueFlags() {
        return valueFlagMap;
    }
    
    public boolean hasBooleanFlag(String flag) {
        return booleanFlags.contains(flag);
    }
    
    public String getFlagValue(String flag) {
        return valueFlagMap.get(flag);
    }
    
    public boolean hasFlagValue(String flag) {
        return valueFlagMap.containsKey(flag);
    }
    
    public List<String> getUnprocessedArgs() {
        return nonProcessedArguments;
    }
    
    public String[] getUnprocessedArgArray() {
        return nonProcessedArguments.toArray(new String[nonProcessedArguments.size()]);
    }
    
    @Deprecated
    @Useless
    public class UnclosedStringException extends Exception {
        public UnclosedStringException(String unclosedString) {
            super(unclosedString);
        }
    }
}
