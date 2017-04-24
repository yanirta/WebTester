package com.applitools.Commands;


import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.TestResults;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.Strings;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import static javax.xml.parsers.DocumentBuilderFactory.newInstance;

@Parameters(commandDescription = "Iterate over a list of urls set in .xml file like sitemap.xml")
public class IterativeTest extends ApplitoolsTest {
    private static final String BUGIFY_FILENAME = "bugify.js";

    @Parameter(names = {"-lo", "--location"}, required = true,
            description = "Set website url or a path to sitemap file. \n" +
                    "If provided implicitly, will search for sitemap.xml in the provided location.")
    private String location;

    private URI locationUrl_;
    private DocumentBuilder builder_;
    private String bugify_js_ = null;
    private List<URI> pages_;

    @Override
    public void Init() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        super.Init();
        if (!location.endsWith(".xml")) {
            location += !location.endsWith("/") ? "/" : "";
            location += "sitemap.xml";
        }

        locationUrl_ = new URI(location);
        init_urls(locationUrl_);

        init_bugify();
    }

    private void init_urls(URI locationUrl) throws ParserConfigurationException, SAXException, IOException {
        builder_ = newInstance().newDocumentBuilder();
        Element document = builder_.parse(locationUrl.toURL().openStream()).getDocumentElement();
        NodeList urlset = document.getElementsByTagName("loc");
        pages_ = new LinkedList<URI>();

        if (eyes_.getBatch() == null)
            if (locationUrl_.getHost() != null)
                eyes_.setBatch(new BatchInfo(locationUrl_.getHost()));
            else
                eyes_.setBatch(new BatchInfo("WebTester Iterative Batch"));

        for (int i = 0; i < urlset.getLength(); ++i) {
            String page = urlset.item(i).getFirstChild().getNodeValue();
            try {
                pages_.add(new URI(page));
            } catch (URISyntaxException e) {
                System.out.printf("Warning! Skipping invalid url: %s \n", page);
            }
        }
    }

    public void Execute() {
        System.out.printf("Starting test on %s \n", location);
        int newtests = 0, failedtests = 0, total = pages_.size();
        int i = 0;
        for (URI page : pages_) {
            String currAppName = (Strings.isNullOrEmpty(appName))
                    ? (page.getHost() != null ? page.getHost() : "WebTester") : appName;
            String testName = Strings.isNullOrEmpty(page.getPath()) ? "/" : page.getPath();
            eyes_.open(driver_, currAppName, testName);
            driver_.get(page.toString());
            bugify(driver_);
            eyes_.checkWindow(page.toString());
            TestResults result = eyes_.close(false);
            ++i;
            printResult(result, testName, i, total);

            if (result.isNew()) ++newtests;
            else if (!result.isPassed()) ++failedtests;
        }

        System.out.format("Test ended, Total Steps - %s, %s-Passed, %s-Failed, %s-New \n\n",
                total, total - newtests - failedtests, failedtests, newtests);
        //TODO exit process with state
    }

    private void printResult(TestResults result, String testName, int i, int total) {
        System.out.printf(" [%s] %s/%s - %s \n", stringStatus(result), i, total, testName);
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

    private void bugify(WebDriver driver) {
        if (null != bugify_js_) {
            try {
                Thread.sleep(3000);
                ((JavascriptExecutor) driver).executeScript(bugify_js_);
            } catch (InterruptedException e) {
                System.out.printf("Interupted exception in sleep \n");
                System.out.printf("%s \n", e.getMessage());
                System.out.printf("Cause %s \n", e.getCause().getMessage());
            }
        }
    }
}
