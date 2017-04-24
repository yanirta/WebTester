package com.applitools.Commands;

import com.applitools.Utils.Validator;
import com.applitools.eyes.TestResults;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.Scanner;

@Parameters(commandDescription = "Open browser and waits for step(s) name(s) to capture screenshot.")
public class InteractiveTest extends ApplitoolsTest {
    private static final String TEST_END = "~";
    private static final Scanner in = new Scanner(System.in);

    @Parameter(names = {"-st", "--singletest"}, description = "Aet all the checkpoints be part of one test.")
    private boolean singleTest = false;

    @Parameter(names = {"-tn", "--testName"}, description = "Applitools test name")
    private String testName = null;

    @Override
    public void ValidateParams() {
        super.ValidateParams();
        Validator.ifFalseThenRequired(singleTest, batchName, "Single test (-st)", "Batch name (-ba)");
        Validator.ifFalseThenNotAllowed(singleTest, testName, "Single test (-st)", "Test name (-tn)");
    }

    public void Execute() {
        if (singleTest) execInTest();
        else execInBatch();
    }

    private void execInBatch() {
        int tests = 0, newtests = 0, failedtests = 0;
        while (true) {
            System.out.printf("(%s) Enter test name ('%s' for exit): ", tests + 1, TEST_END);
            String test = in.nextLine();
            if (test.equals(TEST_END)) break;
            System.out.println("Processing...");
            eyes_.open(driver_, appName, test);
            eyes_.checkWindow(driver_.getCurrentUrl());
            TestResults result = eyes_.close(false);
            System.out.printf("Done - %s \n", stringStatus(result));

            if (result.isNew() || !result.isPassed())
                System.out.printf("See the result in %s \n", result.getUrl());

            if (!result.isPassed())
                if (result.isNew()) ++newtests;
                else ++failedtests;
            ++tests;
        }

        System.out.printf("\nBatch ended!\n");
        System.out.printf("Tests Passed - %s \n", tests - newtests - failedtests);
        System.out.printf("Tests Failed - %s \n", failedtests);
        System.out.printf("New tests - %s \n", newtests);
        System.out.printf("Total - %s \n", tests);
    }

    private void execInTest() {
        eyes_.open(driver_, appName, testName);
        int steps = 0;
        while (true) {
            System.out.printf("(%s) Enter step tag ('%s' for exit): \n", steps + 1, TEST_END);
            String screentag = in.nextLine();
            if (screentag.equals(TEST_END)) break;
            System.out.println("Processing...");
            eyes_.checkWindow(screentag);
            System.out.println("Done");
            ++steps;
        }
        TestResults result = eyes_.close(false);

        if (steps > 0) {
            printResult(result, testName);
        } else {
            System.out.println("No steps in test, nothing to evaluate!");
        }
    }
}
