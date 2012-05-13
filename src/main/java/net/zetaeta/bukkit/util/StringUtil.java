package net.zetaeta.bukkit.util;

public class StringUtil {
    
    public static final String lineSeparator = System.lineSeparator();
    
    /**
     * Concatenates an array of Strings into a single String by putting spaces between them
     * 
     * @param array Array of Strings to be concatenated
     * @return Single String representing the array
     */
    public static String arrayAsString(String... array) {
        if (array.length == 0) {
            return "";
        }
        System.out.println("arrayAsString");
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<array.length - 1; i++) {
            sb.append(array[i]).append(" ");
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }
    
    /**
     * Concatenates an array of Strings into a single String by putting commas and spaces between them
     * 
     * @param array Array of Strings to be concatenated
     * @return Single String representing the array
     */
    public static String arrayAsCommaString(String... array) {
        if (array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<array.length - 1; i++) {
            sb.append(array[i]).append(", ");
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }
    
    public static String arrayAsMultilineString(boolean endLineSep, String... array) {
        if (array.length == 0) {
            if (endLineSep) {
                return System.getProperty("line.separator");
            }
            return "";
        }
        String lineSep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(array[0]);
        for (int i=0; i<array.length - 1; ++i) {
            sb.append(array[i]).append(lineSep);
        }
        if (endLineSep) {
            sb.append(lineSep);
        }
        return sb.toString();
    }
    
    public static String[] stringAsLineArray(String string) {
        return string.split("\r?\n");
    }
    
    /**
     * Concatenates multiple objects into a String using StringBuilder and String.valueOf(Object)
     * 
     * @param args Objects to concat
     * @return String value of Objects concatenated.
     */
    public static String concatString(Object... args) {
        if (args.length == 0)
            return "";
        StringBuilder sb = new StringBuilder(String.valueOf(args[0]));
        for (Object o : args) {
            sb.append(String.valueOf(o));
        }
        return sb.toString();
    }

    /**
     * Concatenates multiple objects into a String using StringBuilder and String.valueOf(Object)
     * 
     * @param args Objects to concat
     * @param bufferLength Amount of prereserved space in the StringBuilder.
     * @return String value of Objects concatenated.
     */
    public static String concatString(int bufferLength, Object... args) {
        if (args.length == 0)
            return "";
        StringBuilder sb = new StringBuilder(bufferLength);
        for (Object o : args) {
            sb.append(String.valueOf(o));
        }
        return sb.toString();
    }
    
    /**
     * Concatenates multiple Strings into a single String using StringBuilder
     * 
     * @param args Strings to concat
     * @return String value of args concatenated.
     */
    public static String concatString(String... args) {
        if (args.length == 0)
            return "";
        StringBuilder sb = new StringBuilder(args.length * 8);
        for (String s : args) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Concatenates multiple Strings into a single String using StringBuilder
     * 
     * @param args Strings to concat
     * @param bufferLength Amount of prereserved space in the StringBuilder.
     * @return String value of args concatenated.
     */
    public static String concatString(int bufferLength, String... args) {
        StringBuilder sb = new StringBuilder(bufferLength);
        for (String s : args) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static String padString(String string, char padder, int size, boolean atEnd) {
        if (string.length() >= size) {
            return string.substring(0, size);
        }
        int charCount = size - string.length();
        StringBuilder sb = new StringBuilder(size);
        if (!atEnd) {
            for (int i=0; i<charCount; ++i) {
                sb.append(padder);
            }
        }
        sb.append(string);
        if (atEnd) {
            for (int i=0; i<charCount; ++i) {
                sb.append(padder);
            }
        }
        return sb.toString();
    }
    
    public static String padString(String string, int size) {
        return padString(string, ' ', size, true);
    }
}
