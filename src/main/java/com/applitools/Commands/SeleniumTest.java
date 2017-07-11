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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
//        Validator.given(capsFile, "Caps file (-cf)").isSetThen()
//                .required(seleniumServerURL, "Selenium server (-se)")
//                .notAllowed(browser, "browser (-br)");

        Validator.given(sessionId, "Session id (-id)")
                .isSetThen()
                .notAllowed(browser, "browser (-br)")
                .notAllowed(capsFile, "Caps file (-cf)");


    }

    @Override
    public void Init() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        super.Init();

        driver_ = InitDriver(sessionId, browser, seleniumServerURL, prepareCapabilities());
    }

    protected WebDriver InitDriver(String sessionId, String browser, String serverUrl, DesiredCapabilities caps) throws IOException {
        if (!Strings.isNullOrEmpty(sessionId)) {
            URL seleniumServer = new URL(Strings.isNullOrEmpty(serverUrl) ? DEFAULT_SERVER : serverUrl);
            return new AttachedWebDriver(seleniumServer, sessionId);
        }

        if (!Strings.isNullOrEmpty(serverUrl))
            return prepRemoteDriver(new URL(serverUrl), caps);

        return prepLocalDriver(browser, caps);
    }

    protected WebDriver prepLocalDriver(String browser, DesiredCapabilities caps) {
        Browser safeBrowser = getSafeBrowser(browser);
        switch (safeBrowser) {
            case Chrome:
                HashMap options = (HashMap) caps.getCapability(ChromeOptions.CAPABILITY);
                options = (options == null) ? new HashMap() : options;
                options.put("args", Arrays.asList(new String[]{"disable-infobars"}));
                caps.setCapability(ChromeOptions.CAPABILITY, options);
                caps.merge(caps);
                return new ChromeDriver(caps);
            case IE:
                return new InternetExplorerDriver(caps);
            case Safari:
                return new SafariDriver(caps);
            case Firefox:
                FirefoxDriver driver = new FirefoxDriver(caps);
                driver.setLogLevel(Level.OFF);
                return driver;
            default:
                return null;
        }
    }

    protected WebDriver prepRemoteDriver(URL serverUrl, DesiredCapabilities caps) {
        return new RemoteWebDriver(serverUrl, caps);
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
            return Browser.valueOf(StringUtils.capitalize(browser.toLowerCase()));
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

    private static void writeCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities("safari", "", Platform.ANY);
        caps.setCapability("platformName", "iOS");
        caps.setCapability("manufacturer", "Apple");
        caps.setCapability("model", "iPhone-5S");
        writeCapabilities(caps, new File("caps.json"));
    }

    public static void writeCapabilities(DesiredCapabilities caps, File destination) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(destination, caps.asMap());
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
