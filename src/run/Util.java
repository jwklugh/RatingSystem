package run;

public class Util {

    public static boolean debugMode = false;

    /**
     * Convince method - Prints the given string to console
     * @param s - The string to print
     */
    public static void p(String s) {
        System.out.println(s);
    }

    /**
     * Prints the given string to console if debug mode is enabled
     * @param s - The string to print
     */
    public static void debug(String s) {
        if (debugMode) {
            System.out.println(s);
        }
    }

}
