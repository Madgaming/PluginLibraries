package net.zetaeta.bukkit.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.zetaeta.bukkit.util.StringUtil;
import net.zetaeta.bukkit.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginConfiguration extends YamlConfiguration {
    private Map<String, String> comments = new HashMap<String, String>();
    private Map<String, String> defaultComments = new HashMap<String, String>();
    private String header = "";
    private String footer = "";
    static Logger log = Bukkit.getLogger();

    public static PluginConfiguration loadConfiguration(InputStream stream) {
        Util.notNull("Stream cannot be null!", stream);
        PluginConfiguration config = new PluginConfiguration();
        try {
            config.load(stream);
        } catch (IOException e) { 
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
        } catch (InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
            e.printStackTrace();
        }
        return config;
    }
    
    public static PluginConfiguration loadConfiguration(File file) {
        Bukkit.getLogger().info("LOADCONFIGURATION");
        Util.notNull("File cannot be null!", file);
        PluginConfiguration config = new PluginConfiguration();
        try {
            config.load(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
        } catch (InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
            e.printStackTrace();
        }
        Bukkit.getLogger().info("loadConfiguration: keys: " + config.getKeys(true));
        return config;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        String[] lines = StringUtil.stringAsLineArray(contents);
        StringBuilder currNodes = new StringBuilder();
        String lastNode = "";
        int currIndent = 0;
        int lastCheckedLine = -1;
        for (int i=0; i<lines.length; ++i) {
            System.out.println("================");
            System.out.println("currNodes = " + currNodes);
            System.out.println("Checking line " + i);
            System.out.println("lines[" + i  + "] = " + lines[i]);
            if (i <= lastCheckedLine) {
                System.out.println("Skipping " + i);
                continue;
            }
            String line = lines[i];
            if (!line.trim().startsWith("#")) { // the line is not a comment
                System.out.println("Line '" + line + "' is not a comment!");
                if (line.trim().isEmpty()) { // empty line
                    System.out.println("Line " + line + " is empty!");
                    lastCheckedLine = i;
                    continue;
                }
                if (!line.contains(":")) { // not a key-value pair
                    System.out.println("Line " + line + " does not contain ':'");
                    lastCheckedLine = i;
                    continue;
                }
                String key = line.split(":")[0].trim();
                System.out.println("key = " + key);
                int indent = 0;
                for (char c : line.toCharArray()) {
                    if (c != ' ') 
                        break;
                    ++indent;
                }
                System.out.println("Indented to " + indent);
                int effectiveIndent = indent / options().indent();
                System.out.println("Effective indent = " + effectiveIndent);
                if (effectiveIndent > currIndent) { // if this node is a subnode of the previous.
                    System.out.println("effectiveIndent > currIndent");
                    currNodes.append('.').append(key);
                    currIndent = effectiveIndent;
                }
                else if (effectiveIndent == currIndent) { // if this node is on the same level as the previous.
                    System.out.println("effectiveIndent == currIndent");
                    currNodes.delete(currNodes.length() - lastNode.length(), currNodes.length());
                    currNodes.append(key);
                    lastNode = key;
                }
                else { // if this node is a a subnode of a supernode of the supernode of the previous node.
                    System.out.println("effectiveIndent < currIndent");
                    int removed = 1 + currIndent - effectiveIndent; // number of indents the previous node was farther than the current, + 1 for the previous node itself
                    String[] reverse = Util.reverse(currNodes.toString().split("\\."));
                    if (reverse.length == 0) {
                        lastCheckedLine = i;
                        continue;
                    }
                    
                    System.out.println("Reverse: {");
                    for (String s : reverse) {
                        System.out.println(s);
                    }
                    System.out.println("}");
                    for (int j=0; j<removed && j<reverse.length; ++j) {
                        System.out.println("Removing: " + reverse[j]);
                        currNodes.delete(currNodes.length() - reverse[j].length(), currNodes.length());
                        if (!(currNodes.length() == 0) && currNodes.charAt(currNodes.length() - 1) == '.') {
                            currNodes.delete(currNodes.length() - 1, currNodes.length());
                        }
                    }
                    currNodes.append('.').append(key);
                    lastNode = key;
                    currIndent = effectiveIndent;
                }
                lastCheckedLine = i;
                continue;
            }
            
            int next = i;
            StringBuilder lineBuilder = new StringBuilder();
            if (i == 0) {
                System.out.println("Starting header loop, lineBuilder == " + lineBuilder + ", line == " + line);
                while (lines.length > next && lines[next].trim().startsWith("#")) {
                    log.info("Looping header: " + lines[next]);
                    log.info("Before: '" + lineBuilder.toString().replace("\r\n", "\\") + "'");
                    log.info("Adding '" + lines[next] + "'");
                    lineBuilder.append(lines[next]);
                    log.info("After: '" + lineBuilder.toString().replace("\r\n", "\\") + "'");
//                    System.out.println("Line separator = '" + System.lineSeparator() + "'");
                    ++next;
                }
                header = lineBuilder.toString();
                lastCheckedLine = next;
                continue;
            }
            next = i + 1;
            if (lines.length <= next) {
                lastCheckedLine = next;
                footer = line;
                continue;
            }
            lineBuilder.append(lines[i]);
            while (lines.length > next && (lines[next].trim().isEmpty() || lines[next].trim().startsWith("#"))) {
                System.out.println("Looping @ PluginConfiguration.java:99");
                System.out.println("next == " + next);
                System.out.println("lines[" + next + "] == " + lines[next]);
                System.out.println("lineBuilder == " + lineBuilder);
//                if (lines[next].trim().startsWith("#")) {
                    lineBuilder.append(StringUtil.lineSeparator).append(lines[next]);
//                }
                ++next;
            }
            if (lines.length == next) {
                System.out.println("lines.length == next");
                System.out.println("next = " + next);
                System.out.println("lineBuilder == " + lineBuilder);
                footer = lineBuilder.toString();
                break;
            }
            System.out.println("New comment: " + lineBuilder + ", getting node...");

            // now next line must be a node
            String nextLine = lines[next];
            
            if (!nextLine.contains(":")) { // not a key
                System.out.println("Line " + line + " does not contain ':'");
                lastCheckedLine = next;
                continue;
            }
            String key = nextLine.split(":")[0].trim();
            System.out.println("key = " + key);
            int indent = 0;
            for (char c : nextLine.toCharArray()) {
                if (c != ' ')
                    break;
                ++indent;
            }
            int effectiveIndent = indent / options().indent();
            if (effectiveIndent > currIndent) { // if this node is a subnode of the previous.
                System.out.println("effectiveIndent > currIndent");
                System.out.println("Appending " + key + " to " + currNodes);
                currNodes.append('.').append(key);
                currIndent = effectiveIndent;
            }
            else if (effectiveIndent == currIndent) { // if this node is on the same level as the previous.
                System.out.println("effectiveIndent == currIndent");
                System.out.println("Replacing " + lastNode + " with " + key);
                currNodes.delete(currNodes.length() - lastNode.length(), currNodes.length());
                currNodes.append(key);
                lastNode = key;
            }
            else { // if this node is a a subnode of a supernode of the supernode of the previous node.
                System.out.println("effectiveIndent < currIndent");
                int removed = currIndent - effectiveIndent;
                String[] reverse = currNodes.reverse().toString().split("\\.");
                if (reverse.length == 0) {
                    lastCheckedLine = next;
                    continue;
                }
                
                System.out.println("Reverse: {");
                for (String s : reverse) {
                    System.out.println(s);
                }
                System.out.println("}");
                currNodes.reverse();
                for (int j=0; j<removed && j<reverse.length; ++j) {
                    System.out.println("Removing: " + reverse[j]);
                    currNodes.delete(currNodes.length() - reverse[j].length(), currNodes.length());
                    if (!(currNodes.length() == 0) && currNodes.charAt(currNodes.length() - 1) == '.') {
                        currNodes.delete(currNodes.length() - 1, currNodes.length());
                    }
                }
                currNodes.append('.').append(key);
                lastNode = key;
                currIndent = effectiveIndent;
            }
            lastCheckedLine = next;
            System.out.println("Adding comment '" + lineBuilder + "' for node '" + currNodes + "'");
            comments.put(currNodes.toString(), lineBuilder.toString());
        }
        
        System.out.println("========FINISHED LOADING FROM STRING========");
        System.out.println(contents);
        
        super.loadFromString(contents);
        System.out.println("All keys: " + getKeys(true));
        System.out.println("Header: " + getHeader());
        System.out.println("Comments: " + comments);
    }

    @Override
    public String saveToString() {
        System.out.println("All keys: " + getKeys(true));
        log.info("Header: '" + getHeader().replace("\r\n", "\\").replace("\n", "\\") + "'");
        log.info("Comments: " + comments);
        String first = super.saveToString();
        System.out.println("super.saveToString() == " + first);
        String[] lines = StringUtil.stringAsLineArray(first);
        StringBuilder currNodes = new StringBuilder();
//        List<String> output = new ArrayList<String>();
        StringBuilder output = new StringBuilder();
        String lastNode = "";
        int currIndent = 0;
        
        output.append(header).append(System.lineSeparator()).append(System.lineSeparator());
        for (int i=0; i< lines.length; ++i) {
            String line = lines[i];
            if (!line.trim().startsWith("#")) { // the line is not a comment
                System.out.println("Line '" + line + "' is not a comment!");
                if (line.trim().isEmpty()) { // empty line
                    System.out.println("Line " + line + " is empty!");
//                    lastCheckedLine = i;
                    continue;
                }
                if (!line.contains(":")) { // not a key-value pair
                    System.out.println("Line " + line + " does not contain ':'");
//                    lastCheckedLine = i;
                    continue;
                }
                String key = line.split(":")[0].trim();
                System.out.println("key = " + key);
                int indent = 0;
                for (char c : line.toCharArray()) {
                    if (c != ' ') 
                        break;
                    ++indent;
                }
                System.out.println("Indented to " + indent);
                int effectiveIndent = indent / options().indent();
                System.out.println("Effective indent = " + effectiveIndent);
                if (effectiveIndent > currIndent) { // if this node is a subnode of the previous.
                    System.out.println("effectiveIndent > currIndent");
                    currNodes.append('.').append(key);
                    currIndent = effectiveIndent;
                }
                else if (effectiveIndent == currIndent) { // if this node is on the same level as the previous.
                    System.out.println("effectiveIndent == currIndent");
                    currNodes.delete(currNodes.length() - lastNode.length(), currNodes.length());
                    currNodes.append(key);
                    lastNode = key;
                }
                else { // if this node is a a subnode of a supernode of the supernode of the previous node.
                    System.out.println("effectiveIndent < currIndent");
                    int removed = 1 + currIndent - effectiveIndent; // number of indents the previous node was farther than the current, + 1 for the previous node itself
                    String[] reverse = Util.reverse(currNodes.toString().split("\\."));
                    
                    System.out.println("Reverse: {");
                    for (String s : reverse) {
                        System.out.println(s);
                    }
                    System.out.println("}");
                    for (int j=0; j<removed && j<reverse.length; ++j) {
                        System.out.println("Removing: " + reverse[j]);
                        if (!(currNodes.length() == 0) && currNodes.charAt(currNodes.length() - 1) == '.') {
                            currNodes.delete(currNodes.length() - 1, currNodes.length());
                        }
                    }
                    currNodes.append('.').append(key);
                    lastNode = key;
                    currIndent = effectiveIndent;
                }
//                lastCheckedLine = i;
//                continue;
            }
            
            String comment = comments.get(currNodes.toString());
            
            if (comment == null) {
                if ((comment = defaultComments.get(currNodes.toString())) == null) {
                    System.out.println("No comment for " + currNodes.toString());
                    output.append(line).append('\n');
                    continue;
                }
            }
            output.append(comment).append('\n').append(line).append('\n');
        }
        if (footer != null && !footer.isEmpty()) {
            output.append('\n').append(footer);
        }
        
        System.out.println("FILE AS STRING == " + output);
        return output.toString();
    }

    @Override
    public String buildHeader() {
        return "";
//        return header == null ? "" : header + System.lineSeparator();
    }

    @Override
    protected String parseHeader(String input) {
        return header;
    }
    
    @Override
    public void addDefaults(Configuration defaults) {
        if (defaults instanceof PluginConfiguration) {
            addDefaultComments(((PluginConfiguration) defaults).comments);
        }
        super.addDefaults(defaults);
    }

    @Override
    public void setDefaults(Configuration defaults) {
        if (defaults instanceof PluginConfiguration) {
            setDefaultComments(((PluginConfiguration) defaults).comments);
        }
        super.setDefaults(defaults);
    }

    public void addDefaultComments(Map<String, String> defaults) {
        defaultComments.putAll(defaults);
    }
    
    public void setDefaultComments(Map<String, String> defaults) {
        defaultComments = defaults;
    }
    
    public void addComment(String node, String comment) {
        comments.put(node, comment);
    }
    
    public void addDefaultComment(String node, String comment) {
        defaultComments.put(node, comment);
    }

    public String getHeader() {
        return header;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public String getFooter() {
        return footer;
    }
}
