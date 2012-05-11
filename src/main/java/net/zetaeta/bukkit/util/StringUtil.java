package net.zetaeta.bukkit.util;

public class StringUtil {
    /**
     * Concatenates an array of Strings into a single String by putting spaces between them
     * 
     * @param array Array of Strings to be concatenated
     * @return Single String representing the array
     */
    public static String arrayAsString(String[] array) {
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
    public static String arrayAsCommaString(String[] array) {
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
}
