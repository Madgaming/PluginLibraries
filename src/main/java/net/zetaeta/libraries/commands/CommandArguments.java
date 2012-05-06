package net.zetaeta.libraries.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.zetaeta.libraries.Useless;
import net.zetaeta.libraries.ZPUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Daniel
 *
 */
public class CommandArguments {
    
    private String[] rawArgs;
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
        rawArgs = arguments;
        booleanFlags = new HashSet<String>();
        valueFlagMap = new HashMap<String, String>();
        nonProcessedArguments = new ArrayList<String>();
    }
    
    /**
     * Constructs a CommandArgumentsTest with the parameter String split by spaces.
     * Equivalent to {@link #CommandArguments(String[]) CommandArgumentsTest(arguments.split(" ")}.
     * 
     * @param arguments String to split and process.
     */
    public CommandArguments(String arguments) {
        this(arguments.split(" "));
    }
    
    /**
     * Constructs a CommandArgumentsTest with the parameter String split by the parameter split String.
     * Equivalent to {@link #CommandArguments(String[]) CommandArgumentsTest(arguments.split(split)}.
     * 
     * @param arguments String to split and process.
     * @param split Regex to split arguments by.
     */
    public CommandArguments(String arguments, String split) {
        this(arguments.split(split));
    }
    
    public static CommandArguments processArguments(String alias, String[] args, String[] boolFlags, String[] valueFlags) {
        CommandArguments cArgs = new CommandArguments(args);
        if (cArgs.process(Arrays.asList(boolFlags), Arrays.asList(valueFlags))) {
            return cArgs;
        }
        return null;
    }
    
    public static CommandArguments processArguments(String alias, String[] args, String[] boolFlags, String[] valueFlags, CommandSender errorReciever) {
        CommandArguments cArgs = processArguments(alias, args, boolFlags, valueFlags);
        if (cArgs == null) {
            errorReciever.sendMessage("§cYour command " + ZPUtil.arrayAsString(args) + " could not be processed.");
        }
        return cArgs;
    }
    
    /**
     * Processes the CommandArgumentsTest
     *
     * @param boolFlags
     * @param valueFlags
     * @return True if parsing was successful, false otherwise. 
     */
    public boolean process(Collection<String> boolFlags, Collection<String> valueFlags) {
        int lastprocessed = -1;
        for (int i=0; i<rawArgs.length; ++i) {
            if (i <= lastprocessed) {
                continue;
            }
            if (rawArgs[i].startsWith("-")) {
                if (boolFlags.contains(rawArgs[i].substring(1))) {
                    booleanFlags.add(rawArgs[i].substring(1));
                }
                else if (valueFlags.contains(rawArgs[i].substring(1))) {
                    
                    if (rawArgs.length == i + 1) {
                        return true;
                    }
                    if (rawArgs[i + 1].startsWith("\"")) {
                        if (rawArgs[i + 1].endsWith("\"")) {
                            valueFlagMap.put(rawArgs[i], rawArgs[i + 1].substring(1, rawArgs[i + 1].length() - 1));
                            lastprocessed = i + 1;
                            continue;
                        }
                        if (rawArgs.length <= i + 2) {
                            continue;
                        }
                        StringBuilder value = new StringBuilder((rawArgs.length - i) * 10);
                        value.append(rawArgs[i + 1].substring(1));
                        for (int j=i + 2; j<rawArgs.length; j++) {
                            if (rawArgs[j].endsWith("\"")) {
                                value.append(' ').append(rawArgs[j].substring(0, rawArgs[j].length() - 1));
                                lastprocessed = j;
                                break;
                            }
                            value.append(' ').append(rawArgs[j]);
                            lastprocessed = j;
                            if (j == rawArgs.length - 1) { // if this is the last index
                                return false;
                            }
                        }
                        valueFlagMap.put(rawArgs[i].substring(1), value.toString());
                    }
                    else if (rawArgs[i + 1].startsWith("'")) {
                        if (rawArgs[i + 1].endsWith("'")) {
                            valueFlagMap.put(rawArgs[i].substring(1), rawArgs[i + 1].substring(1, rawArgs[i + 1].length() - 1));
                            lastprocessed = i + 1;
                            continue;
                        }
                        if (rawArgs.length <= i + 2) {
                            continue;
                        }
                        StringBuilder value = new StringBuilder(rawArgs.length - i * 10);
                        value.append(rawArgs[i + 1]);
                        for (int j=i + 2; j<rawArgs.length; j++) {
                            if (rawArgs[j].endsWith("'")) {
                                value.append(rawArgs[j].substring(0, rawArgs[j].length()));
                                lastprocessed = j;
                                break;
                            }
                            value.append(' ').append(rawArgs[j]);
                            lastprocessed = j;
                            if (j == rawArgs.length - 1) { // if this is the last index
                                return false;
                            }
                        }
                        valueFlagMap.put(rawArgs[i].substring(1), value.toString());
                    }
                    else {
//                        if (hasMoreFlags(Arrays.copyOfRange(rawArgs, i, rawArgs.length))) {
//                            valueFlagMap.put(rawArgs[i], ZPUtil.arrayAsString(Arrays.copyOfRange(rawArgs, i, rawArgs.length)));
//                        }
                        valueFlagMap.put(rawArgs[i].substring(1), rawArgs[i + 1]);
                        lastprocessed = i + 1;
                    }
                }
            }
            else {
                nonProcessedArguments.add(rawArgs[i]);
            }
        }
        processed = true;
        return true;
        
    }
    
    public boolean hasMoreFlags(String[] array) {
        for (String s : array) {
            if (s.startsWith("-")) {
                return true;
            }
        }
        return false;
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
    
    public String[] getRawArgs() {
        return rawArgs;
    }
    
    @Deprecated
    @Useless
    public class UnclosedStringException extends Exception {
        public UnclosedStringException(String unclosedString) {
            super(unclosedString);
        }
    }
}
