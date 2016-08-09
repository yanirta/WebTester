package com.applitools.Modes.InteractiveSession;

import com.applitools.Modes.Session;
import com.applitools.eyes.TestResults;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Scanner;

public class InteractiveTest extends Session {
    static final String TEST_END = "~";

    public static Options options = buildOptions();
    private static Scanner in = new Scanner(System.in);

    public InteractiveTest(PrintStream out, String[] args) throws ParseException, URISyntaxException, IOException {
        super(out, args);
        println("Running Interactive mode:");
    }


    public void run() {
        String testName = cmd_.getOptionValue("t", "Default Test Name");
        eyes_.open(driver_, appName_, testName);

        int steps = 1;
        while (true) {
            printf("(%s) Enter step tag ('%s' for exit): ", steps, TEST_END);
            String screentag = in.nextLine();
            if (screentag.toLowerCase().equals(TEST_END)) break;
            printf("Processing...");
            eyes_.checkWindow(screentag);
            printf("Done\n");
            ++steps;
        }

        printf("Calculating results...\n");
        TestResults result = eyes_.close(false);
        printResults(result);
    }

    private void printResults(TestResults result) {
        if (result.isNew()) {
            println("--- New Test - Created new baseline! ---");
        } else if (result.isPassed()) {
            println("--- Test %sPassed!%s ---");
        } else {
            println("--- Test %sFailed!%s ---");
        }

        printf("To see the results in Applitools please go to:\n%s\n", result.getUrl());
    }

    protected Options getOptions() {
        return options;
    }

    protected String getDefaultAppname() {
        return "Applitools WebTester";
    }

    private static Options buildOptions() {
        Options options = Session.BuildSharedOptions();

        return options;
    }
}
