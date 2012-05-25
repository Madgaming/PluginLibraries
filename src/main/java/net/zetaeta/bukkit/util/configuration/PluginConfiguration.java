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
            if (i <= lastCheckedLine) {
                continue;
            }
            String line = lines[i];
            if (!line.trim().startsWith("#")) { // the line is not a comment
                if (line.trim().isEmpty()) { // empty line
                    lastCheckedLine = i;
                    continue;
                }
                if (!line.contains(":")) { // not a key-value pair
                    lastCheckedLine = i;
                    continue;
                }
                String key = line.split(":")[0].trim();
                int indent = 0;
                for (char c : line.toCharArray()) {
                    if (c != ' ') 
                        break;
                    ++indent;
                }
                int effectiveIndent = indent / options().indent();
                if (effectiveIndent > currIndent) { // if this node is a subnode of the previous.
                    currNodes.append('.').append(key);
                    currIndent = effectiveIndent;
                }
                else if (effectiveIndent == currIndent) { // if this node is on the same level as the previous.
                    currNodes.delete(currNodes.length() - lastNode.length(), currNodes.length());
                    currNodes.append(key);
                    lastNode = key;
                }
                else { // if this node is a a subnode of a supernode of the supernode of the previous node.
                    int removed = 1 + currIndent - effectiveIndent; // number of indents the previous node was farther than the current, + 1 for the previous node itself
                    String[] reverse = Util.reverse(currNodes.toString().split("\\."));
                    if (reverse.length == 0) {
                        lastCheckedLine = i;
                        continue;
                    }
                    
                    for (int j=0; j<removed && j<reverse.length; ++j) {
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
                while (lines.length > next && lines[next].trim().startsWith("#")) {
                    lineBuilder.append(lines[next]);
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
                lineBuilder.append(StringUtil.lineSeparator).append(lines[next]);
                ++next;
            }
            if (lines.length == next) {
                footer = lineBuilder.toString();
                break;
            }
            // now next line must be a node
            String nextLine = lines[next];
            
            if (!nextLine.contains(":")) { // not a key
                lastCheckedLine = next;
                continue;
            }
            String key = nextLine.split(":")[0].trim();
            int indent = 0;
            for (char c : nextLine.toCharArray()) {
                if (c != ' ')
                    break;
                ++indent;
            }
            int effectiveIndent = indent / options().indent();
            if (effectiveIndent > currIndent) { // if this node is a subnode of the previous.
                currNodes.append('.').append(key);
                currIndent = effectiveIndent;
            }
            else if (effectiveIndent == currIndent) { // if this node is on the same level as the previous.
                currNodes.delete(currNodes.length() - lastNode.length(), currNodes.length());
                currNodes.append(key);
                lastNode = key;
            }
            else { // if this node is a a subnode of a supernode of the supernode of the previous node.
                int removed = currIndent - effectiveIndent;
                String[] reverse = currNodes.reverse().toString().split("\\.");
                if (reverse.length == 0) {
                    lastCheckedLine = next;
                    continue;
                }
                
                currNodes.reverse();
                for (int j=0; j<removed && j<reverse.length; ++j) {
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
            comments.put(currNodes.toString(), lineBuilder.toString());
        }
        
        super.loadFromString(contents);
    }

    @Override
    public String saveToString() {
        String first = super.saveToString();
        String[] lines = StringUtil.stringAsLineArray(first);
        StringBuilder currNodes = new StringBuilder();
        StringBuilder output = new StringBuilder();
        String lastNode = "";
        int currIndent = 0;
        
        output.append(header).append(System.lineSeparator()).append(System.lineSeparator());
        for (int i=0; i< lines.length; ++i) {
            String line = lines[i];
            if (!line.trim().startsWith("#")) { // the line is not a comment
                if (line.trim().isEmpty()) { // empty line
                    continue;
                }
                if (!line.contains(":")) { // not a key-value pair
                    continue;
                }
                String key = line.split(":")[0].trim();
                int indent = 0;
                for (char c : line.toCharArray()) {
                    if (c != ' ') 
                        break;
                    ++indent;
                }
                int effectiveIndent = indent / options().indent();
                if (effectiveIndent > currIndent) { // if this node is a subnode of the previous.
                    currNodes.append('.').append(key);
                    currIndent = effectiveIndent;
                }
                else if (effectiveIndent == currIndent) { // if this node is on the same level as the previous.
                    currNodes.delete(currNodes.length() - lastNode.length(), currNodes.length());
                    currNodes.append(key);
                    lastNode = key;
                }
                else { // if this node is a a subnode of a supernode of the supernode of the previous node.
                    int removed = 1 + currIndent - effectiveIndent; // number of indents the previous node was farther than the current, + 1 for the previous node itself
                    String[] reverse = Util.reverse(currNodes.toString().split("\\."));
                    
                    for (int j=0; j<removed && j<reverse.length; ++j) {
                        if (!(currNodes.length() == 0) && currNodes.charAt(currNodes.length() - 1) == '.') {
                            currNodes.delete(currNodes.length() - 1, currNodes.length());
                        }
                    }
                    currNodes.append('.').append(key);
                    lastNode = key;
                    currIndent = effectiveIndent;
                }
            }
            
            String comment = comments.get(currNodes.toString());
            
            if (comment == null) {
                if ((comment = defaultComments.get(currNodes.toString())) == null) {
                    output.append(line).append('\n');
                    continue;
                }
            }
            output.append(comment).append('\n').append(line).append('\n');
        }
        if (footer != null && !footer.isEmpty()) {
            output.append('\n').append(footer);
        }
        return output.toString();
    }

    @Override
    public String buildHeader() {
        return "";
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
