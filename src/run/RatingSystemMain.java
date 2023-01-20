package run;

import ui.MainUI;

/**
 * 
 * @author Jason
 *
 */
public class RatingSystemMain {

    static Runner runner; // The runner for the program
    static MainUI ui; // The UI for the program - package visible for runner

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        Util.debugMode = true;
        start();
    }

    /**
     * 
     */
    private static void start() {
        runner = Runner.getRunner();
        ui = new MainUI();
    }
}
