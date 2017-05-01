package com.applitools.Commands;

import com.applitools.Browser;
import com.applitools.Modes.AttachedSession.AttachedWebDriver;
import com.applitools.Utils.Validator;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.sun.xml.internal.ws.util.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public abstract class SeleniumTest extends Test {
    private static final String DEFAULT_SERVER = "http://localhost:4444/wd/hub/";
    private static final Browser DEFAULT_BROWSER = Browser.Firefox;

    //Browser
    @Parameter(names = {"-br", "--browser"}, description = "Specify browser from [Chrome|Firefox|Safari|IE] default = Firefox")
    protected String browser;

    //Selenium server
    @Parameter(names = {"-se", "--seleniumServer"}, description = "Specify selenium server url")
    protected String seleniumServerURL;

    //Caps file
    @Parameter(names = {"-cf", "--capsFile"}, description = "Specify capabilities json file")
    protected String capsFile;

    //Session id to attach
    @Parameter(names = {"-id", "--sessionId"}, description = "Selenium session-id to attach the test", hidden = true)
//TODO
    protected String sessionId;

    protected WebDriver driver_;

    @Override
    public void ValidateParams() {
        Validator.givenString(capsFile, "Caps file (-cf)").isSetThen()
                .required(seleniumServerURL, "Selenium server (-se)")
                .notAllowed(browser, "browser (-br)");

        Validator.givenString(sessionId, "Session id (-id)")
                .notAllowed(browser, "browser (-br)")
                .notAllowed(capsFile, "Caps file (-cf)");
    }

    @Override
    public void Init() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        if (Strings.isNullOrEmpty(browser)) browser = "Firefox";
        if (!Strings.isNullOrEmpty(sessionId)) {
            if (Strings.isNullOrEmpty(seleniumServerURL)) seleniumServerURL = DEFAULT_SERVER;
            driver_ = new AttachedWebDriver(new URL(seleniumServerURL), sessionId);
        } else if (!Strings.isNullOrEmpty(seleniumServerURL)) {
            URL server = new URL(seleniumServerURL);
            DesiredCapabilities caps = prepareCapabilities();
            driver_ = prepBrowser(server, caps);
        } else {
            driver_ = prepBrowser(getSafeBrowser(browser));
        }
    }

    private DesiredCapabilities prepareCapabilities() throws IOException {
        if (!Strings.isNullOrEmpty(capsFile))
            return readCapabilities(capsFile);
        else
            return prepareCaps(getSafeBrowser(browser));
    }

    private static DesiredCapabilities readCapabilities(String capsfile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return new DesiredCapabilities(
                (Map<String, ?>) mapper.readValue(
                        new File(capsfile),
                        new TypeReference<Map<String, Object>>() {
                        }));
    }

    private static Browser getSafeBrowser(String browser) throws IllegalArgumentException {
        try {
            if (Strings.isNullOrEmpty(browser)) return DEFAULT_BROWSER;
            return Browser.valueOf(StringUtils.capitalize(browser));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong browser argument");
        }
    }

    private static WebDriver prepBrowser(URL server, DesiredCapabilities caps) {
        return new RemoteWebDriver(server, caps);
    }

    private static DesiredCapabilities prepareCaps(Browser browser) {

        switch (browser) {
            case Chrome:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("disable-infobars");
                DesiredCapabilities ds = DesiredCapabilities.chrome();
                ds.setCapability(ChromeOptions.CAPABILITY, options);
                return ds;
            case IE:
                return DesiredCapabilities.internetExplorer();
            case Safari:
                return DesiredCapabilities.safari();
            case Firefox:
                return DesiredCapabilities.firefox();
        }
        return new DesiredCapabilities();
    }

    private static WebDriver prepBrowser(Browser browser) {
        switch (browser) {
            case Chrome:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("disable-infobars");
                return new ChromeDriver(options);
            case IE:
                return new InternetExplorerDriver();
            case Safari:
                return new SafariDriver();
            case Firefox:
                FirefoxDriver driver = new FirefoxDriver();
                return driver;
            default:
                return null;
        }
    }

    private static void writeCapabilities() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DesiredCapabilities caps = new DesiredCapabilities("safari", "", Platform.ANY);
            caps.setCapability("platformName", "iOS");
            caps.setCapability("manufacturer", "Apple");
            caps.setCapability("model", "iPhone-5S");
            mapper.writeValue(new File("caps.json"), caps.asMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void TearDown() {
        if (driver_ != null)
            driver_.quit();
    }
}
