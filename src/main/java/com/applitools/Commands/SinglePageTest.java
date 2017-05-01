package com.applitools.Commands;

import com.applitools.Utils.Validator;
import com.applitools.eyes.TestResults;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.Strings;

@Parameters(commandDescription = "Perform validation on a single page")
public class SinglePageTest extends ApplitoolsTest {

    @Parameter(names = {"-tn", "--testName"}, description = "Applitools test name")
    private String testName;

    @Parameter(names = {"-pu", "--pageURL"}, description = "Set the page to open before testing")
    private String pageURL;

    @Override
    public void ValidateParams() {
        super.ValidateParams();
        Validator.givenString(testName, "Test name (-tn)").isNotSetThen()
                .required(pageURL, "Page url (-pu)");
    }

    public void Execute() {
        if (!Strings.isNullOrEmpty(pageURL)) driver_.get(pageURL);
        if (Strings.isNullOrEmpty(testName)) testName = pageURL;

        eyes_.open(driver_, appName, testName);
        eyes_.checkWindow();
        TestResults result = eyes_.close(false);

        printResult(result, testName);
    }
}
