package com.applitools.Modes.InteractiveSession;


public class InteractiveTest {
//    static final String TEST_END = "~";
//    private static Scanner in = new Scanner(System.in);
//
//    public InteractiveTest(PrintStream out, String[] args) {
//        super(out, args);
//        println("Running Interactive mode:");
//    }
//
//
//    public void run() {
//        String testName = cmd_.getOptionValue("t", "Default Test Name");
//        eyes_.open(driver_, appName_, testName);
//
//        int steps = 0;
//        while (true) {
//            printf("(%s) Enter step tag ('%s' for exit): ", steps + 1, TEST_END);
//            String screentag = in.nextLine();
//            if (screentag.toLowerCase().equals(TEST_END)) break;
//            printf("Processing...");
//            eyes_.checkWindow(screentag);
//            printf("Done\n");
//            ++steps;
//        }
//
//        printf("Calculating results...\n");
//        TestResults result = eyes_.close(false);
//
//        if (steps > 0) {
//            printResults(result);
//        } else {
//            printf("No steps in test, nothing to evaluate!");
//        }
//    }
//
//    private void printResults(TestResults result) {
//        if (result.isNew()) {
//            println("--- New Test - Created new baseline! ---");
//        } else if (result.isPassed()) {
//            println("--- Test %sPassed!%s ---");
//        } else {
//            println("--- Test %sFailed!%s ---");
//        }
//
//        printf("To see the results in Applitools please go to:\n%s\n", result.getUrl());
//    }
//
//    protected String getDefaultAppname() {
//        return "Applitools WebTester";
//    }
//
//    protected Commands getMode() {
//        return Commands.Interactive;
//    }
//
//    protected void addOptions(Options options) {
//        //Do nothing
//    }
}
