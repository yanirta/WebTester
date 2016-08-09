package com.applitools.Modes.EnumeratedSession;

import com.applitools.Modes.Session;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.TestResults;
import com.google.common.base.Strings;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static javax.xml.parsers.DocumentBuilderFactory.*;

public class EnumeratedTest extends Session {
    private static final String BUGIFY_FILENAME = "bugify.js";
    public static final Options options = buildOptions();

    private final DocumentBuilder builder_ = newInstance().newDocumentBuilder();

    private URI url_;
    private String bugify_js_ = null;

    public EnumeratedTest(PrintStream out, String[] args) throws ParseException, URISyntaxException, IOException, ParserConfigurationException {
        super(out, args);

        init_bugify();

        url_ = new URI(cmd_.getOptionValue("l"));
        if (!url_.getPath().toLowerCase().endsWith(".xml"))
            url_ = new URI(url_.getScheme(), url_.getHost(), url_.getPath() + "sitemap.xml", null);
    }

    public void run() throws MalformedURLException {
        Element document = null;
        try {
            document = builder_.parse(url_.toURL().openStream()).getDocumentElement();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NodeList urlset = document.getElementsByTagName("loc");

        if (eyes_.getBatch() == null)
            if (url_.getHost() != null)
                eyes_.setBatch(new BatchInfo(url_.getHost()));
            else
                eyes_.setBatch(new BatchInfo("Web-Tester-Enumerator"));

        printf("Starting test on %s \n", cmd_.getOptionValue("l"));
        int newtests = 0, failedtests = 0, total = urlset.getLength();

        for (int i = 0; i < total; ++i) {
            String url = urlset.item(i).getFirstChild().getNodeValue();
            URL pageurl = new URL(url);

            appName_ = (appName_ == null) ? (url_.getHost() != null ? url_.getHost() : "WebTester") : appName_;
            String testName = pageurl.getPath();
            testName = Strings.isNullOrEmpty(testName) ? "/" : testName;
            eyes_.open(driver_, appName_, testName);
            driver_.get(url);
            try {
                bugify(driver_);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eyes_.checkWindow(url);
            TestResults result = eyes_.close(false);

            printTestSummary(result, i, total, testName);

            if (result.isNew()) {
                ++newtests;
            } else if (!result.isPassed()) {
                ++failedtests;
            }

        }
        printf("Test ended, Total Steps - %s, %s-Passed, %s-Failed, %s-New \n\n", total, total - newtests - failedtests, failedtests, newtests);
        //TODO exit process with state
    }

    private void printTestSummary(TestResults result, int i, int total, String testname) {
        String resultstr = "Passed";
        if (result.isNew()) {
            resultstr = "New";
        } else if (!result.isPassed()) {
            resultstr = "Failed";
        }
        printf("[%s] - page %s/%s - %s \n", resultstr, i + 1, total, testname);
    }

    private void bugify(WebDriver driver) throws InterruptedException {
        if (null != bugify_js_) {
            Thread.sleep(3000);
            ((JavascriptExecutor) driver).executeScript(bugify_js_);
        }
    }

    private void init_bugify() throws IOException {
        File bugifyfile = new File(BUGIFY_FILENAME);
        if (bugifyfile.exists()) {
            System.out.printf("Using %s \n", BUGIFY_FILENAME);
            FileInputStream inputStream = new FileInputStream(bugifyfile);
            try {
                bugify_js_ = IOUtils.toString(inputStream);
            } finally {
                inputStream.close();
            }
        }
    }

    protected Options getOptions() {
        return options;
    }

    protected String getDefaultAppname() {
        return null;
    }

    private static Options buildOptions() {
        Options options = Session.BuildSharedOptions();
        options.addOption(Option.builder("l")
                .longOpt("location")
                .argName("url/path")
                .desc("Set website url or a path to url-list file. \n" +
                        "If provided implicitly, will search for sitemap.xml in the provided path.")
                .hasArg()
                .required()
                .build()
        );

        return options;
    }
}
