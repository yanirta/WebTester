package com.applitools.Modes.AttachedSession;

import com.applitools.Modes.Session;
import com.applitools.eyes.TestResults;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class AttachedTest extends Session {
    public static Options options = buildOptions();

    public AttachedTest(PrintStream out, String[] args) throws ParseException, URISyntaxException, IOException {
        super(out, args);
        println("Running in Attached mode:");
    }

    public void run() {
        String testName = cmd_.getOptionValue("t", null);
        if (testName == null)
            try {
                testName = new URL(driver_.getCurrentUrl()).getHost();
            } catch (MalformedURLException e) {
                //Noting TODO
            }
        //If still null
        if (testName == null) testName = "Attached session fallback test name";

        eyes_.open(driver_, appName_, testName);
        eyes_.checkWindow();
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

    @Override
    protected WebDriver createDriver(CommandLine cmd) throws MalformedURLException {
        String sessionId = cmd.getOptionValue("id");
        URL selenumURL = new URL(cmd.getOptionValue("su"));
        return new AttachedWebDriver(selenumURL, sessionId);
    }

    protected Options getOptions() {
        return options;
    }

    protected String getDefaultAppname() {
        return "Web-Tester";
    }

    private static Options buildOptions() {
        Options options = Session.BuildSharedOptions();
        options.addOption(Option.builder("id")
                .longOpt("seleniumSid")
                .argName("id")
                .desc("Selenium session-id to attach the test")
                .required()
                .hasArg()
                .build());

        options.addOption(Option.builder("su")
                .longOpt("seleniumUrl")
                .argName("url")
                .desc("Selenium server url")
                .required()
                .hasArg()
                .build());

        options.addOption(Option.builder("t")
                .longOpt("testName")
                .argName("name")
                .desc("Applitools test name")
                .hasArg()
                .build());
        return options;
    }
}
