package com.applitools.Commands;


import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.TestResults;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.Strings;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import static javax.xml.parsers.DocumentBuilderFactory.newInstance;

@Parameters(commandDescription = "Iterate over a list of urls set in .xml file like sitemap.xml")
public class IterativeTest extends ApplitoolsTest {

    @Parameter(names = {"-lo", "--location"}, required = true,
            description = "Set website url or a path to sitemap file. \n" +
                    "If provided implicitly, will search for sitemap.xml in the provided location.")
    private String location;

    private URI locationUrl_;
    private DocumentBuilder builder_;
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

            eyesOpen(currAppName, testName);
            driver_.get(page.toString());
            eyesCheck(page.toString());

            TestResults result = eyes_.close(false);
            ++i;
            printResult(result, testName, i, total);

            if (result.isNew()) ++newtests;
            else if (!result.isPassed()) ++failedtests;
        }

        System.out.format("Test ended, Total Steps - %s, %s-Passed, %s-Failed, %s-New \n\n",
                total, total - newtests - failedtests, failedtests, newtests);
        //TODO print batch url
        //TODO exit process with state
    }

    private void printResult(TestResults result, String testName, int i, int total) {
        System.out.printf(" [%s] %s/%s - %s \n", stringStatus(result), i, total, testName);
    }
}
