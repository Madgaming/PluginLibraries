package net.zetaeta.libraries.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.zetaeta.libraries.ZPUtil;

import org.bukkit.command.CommandSender;
import org.junit.Test;

public class CommandArgumentsTest {

private static Logger log = Logger.getLogger("CommandArguments");
    
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
//    public CommandArgumentsTest(String[] arguments) {
//        args = arguments;
//        booleanFlags = new HashSet<String>();
//        valueFlagMap = new HashMap<String, String>();
//        nonProcessedArguments = new ArrayList<String>();
//    }
    
    public CommandArgumentsTest() {
        args = new String[] {};
        booleanFlags = new HashSet<String>();
        valueFlagMap = new HashMap<String, String>();
    }
    
    @Test
    public void testProcessing() {
        processArguments(new String[] {"hello", "-b", "-v", "\"HAM\""}, new String[] {"b"}, new String[] {"v"});
    }
    
    /**
     * Constructs a CommandArgumentsTest with the parameter String split by spaces.
     * Equivalent to {@link #CommandArgumentsTest(String[]) CommandArgumentsTest(arguments.split(" ")}.
     * 
     * @param arguments String to split and process.
     */
//    public CommandArgumentsTest(String arguments) {
//        this(arguments.split(" "));
//    }
    
    /**
     * Constructs a CommandArgumentsTest with the parameter String split by the parameter split String.
     * Equivalent to {@link #CommandArgumentsTest(String[]) CommandArgumentsTest(arguments.split(split)}.
     * 
     * @param arguments String to split and process.
     * @param split Regex to split arguments by.
     */
//    public CommandArgumentsTest(String arguments, String split) {
//        this(arguments.split(split));
//    }
    
    public static CommandArgumentsTest processArguments(String[] args, String[] boolFlags, String[] valueFlags) {
        log.info(ZPUtil.arrayAsString(args));
        log.info(ZPUtil.arrayAsString(boolFlags));
        log.info(ZPUtil.arrayAsString(valueFlags));
        CommandArgumentsTest cArgs = new CommandArgumentsTest(args);
        if (cArgs.process(Arrays.asList(boolFlags), Arrays.asList(valueFlags))) {
            return cArgs;
        }
        return null;
    }
    
    public static CommandArgumentsTest processArguments(String[] args, String[] boolFlags, String[] valueFlags, CommandSender errorReciever) {
        CommandArgumentsTest cArgs = processArguments(args, boolFlags, valueFlags);
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
        for (int i=0; i<args.length; ++i) {
            if (i <= lastprocessed) {
                continue;
            }
            if (args[i].startsWith("-")) {
                if (boolFlags.contains(args[i].substring(1))) {
                    booleanFlags.add(args[i].substring(1));
                    log.info("Adding boolFlag: " + args[i]);
                }
                else if (valueFlags.contains(args[i].substring(1))) {
                    
                    if (args.length == i + 1) {
                        return true;
                    }
                    if (args[i + 1].startsWith("\"")) {
                        if (args[i + 1].endsWith("\"")) {
                            valueFlagMap.put(args[i], args[i + 1].substring(1, args[i + 1].length() - 1));
                            log.info("Adding value flag: " + args[i] + " = " + args[i + 1].substring(1, args[i + 1].length() - 1));
                            lastprocessed = i + 1;
                            continue;
                        }
                        if (args.length <= i + 2) {
                            continue;
                        }
                        StringBuilder value = new StringBuilder((args.length - i) * 10);
                        value.append(args[i + 1].substring(1));
                        for (int j=i + 2; j<args.length; j++) {
                            if (args[j].endsWith("\"")) {
                                log.info("Closing String: \"" + args[j] + "\"");
                                value.append(' ').append(args[j].substring(0, args[j].length() - 1));
                                lastprocessed = j;
                                break;
                            }
                            value.append(' ').append(args[j]);
                            lastprocessed = j;
                            if (j == args.length - 1) { // if this is the last index
                                return false;
                            }
                        }
                        valueFlagMap.put(args[i].substring(1), value.toString());
                        log.info("Adding value flag: " + args[i] + " = " + value.toString());
                    }
                    else if (args[i + 1].startsWith("'")) {
                        if (args[i + 1].endsWith("'")) {
                            valueFlagMap.put(args[i].substring(1), args[i + 1].substring(1, args[i + 1].length() - 1));
                            log.info("Adding value flag: " + args[i] + " = " + args[i + 1].substring(1, args[i + 1].length() - 1));
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
                        valueFlagMap.put(args[i].substring(1), value.toString());
                        log.info("Adding value flag: " + args[i] + " = " + value.toString());
                    }
                    else {
//                        if (hasMoreFlags(Arrays.copyOfRange(args, i, args.length))) {
//                            valueFlagMap.put(args[i], ZPUtil.arrayAsString(Arrays.copyOfRange(args, i, args.length)));
//                        }
                        valueFlagMap.put(args[i].substring(1), args[i + 1]);
                        log.info("Adding value flag: " + args[i] + " = " + args[i + 1]);
                        lastprocessed = i + 1;
                    }
                }
            }
            else {
                nonProcessedArguments.add(args[i]);
                log.info("Added non-processed argument: " + args[i]);
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

}
