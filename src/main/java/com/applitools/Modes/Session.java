package com.applitools.Modes;

import com.applitools.Browser;
import com.applitools.MatchLevels;
import com.applitools.Utils;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.commons.cli.*;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public abstract class Session {
    private final String cur_ver = "0.2"; //TODO find more suitable place and logic
    private final PrintStream out_;
    protected final CommandLine cmd_;
    protected final Eyes eyes_;
    protected final WebDriver driver_;
    protected String appName_;

    public Session(PrintStream out, String[] args) throws ParseException, URISyntaxException, IOException {
        out_ = out;
        cmd_ = Utils.parse(getOptions(), args);

        validate(cmd_);

        driver_ = createDriver(cmd_);
        eyes_ = createEyes(cmd_);
        appName_ = cmd_.getOptionValue("a", getDefaultAppname());
    }

    private Eyes createEyes(CommandLine cmd) throws URISyntaxException, ParseException {
        Eyes eyes = new Eyes() {
            @Override
            public String getBaseAgentId() {
                return String.format("Webtester - %s on %s", cur_ver, super.getBaseAgentId());
            }
        };
        eyes.setApiKey(cmd.getOptionValue("k"));
        eyes.setForceFullPageScreenshot(cmd.hasOption("fp"));
        eyes.setStitchMode(cmd.hasOption("cs") ? StitchMode.CSS : StitchMode.SCROLL);
        eyes.setHideScrollbars(cmd.hasOption("hs"));

        if (cmd.hasOption("as")) eyes.setServerUrl(new URI(cmd.getOptionValue("s")));
        if (cmd.hasOption("w")) eyes.setWaitBeforeScreenshots(Integer.parseInt(cmd.getOptionValue("w")) * 1000);
        if (cmd.hasOption("p")) eyes.setProxy(new ProxySettings(cmd.getOptionValue("p")));
        if (cmd.hasOption("bn")) eyes.setBaselineName(cmd.getOptionValue("bn"));
        if (cmd.hasOption("ba")) eyes.setBatch(createBatchInfo(cmd.getOptionValue("ba")));
        if (cmd.hasOption("cut")) {
            String[] cuts = cmd.getOptionValue("cut").split(":");
            if (cuts.length != 4)
                throw new ParseException("Cut-out parameter (-cut) incorrect");
            eyes.setImageCut(
                    new FixedCutProvider(
                            Integer.parseInt(cuts[0]),
                            Integer.parseInt(cuts[1]),
                            Integer.parseInt(cuts[2]),
                            Integer.parseInt(cuts[3])));
        }
        if (cmd.hasOption("ml")) {
            MatchLevel matchLevel = Utils.parseEnum(MatchLevel.class, cmd.getOptionValue("ml"));
            eyes.setMatchLevel(matchLevel);
        }

        if (cmd.hasOption("vs")) {
            String[] dims = cmd.getOptionValue("vs").split("x");
            if (dims.length != 2)
                throw new ParseException("invalid viewport-size, make sure the call is -vs <width>x<height>");
            eyes.setViewportSize(driver_, new RectangleSize(Integer.parseInt(dims[0]), Integer.parseInt(dims[1])));
        }
        return eyes;
    }

    protected BatchInfo createBatchInfo(String batchParam) {
        String[] batchsplits = batchParam.split(":");
        BatchInfo batch = new BatchInfo(batchsplits[0]);
        if (batchsplits.length == 2) batch.setId(batchsplits[1]);
        return batch;
    }

    protected void println(String msg) {
        out_.println(msg);
    }

    protected void printf(String msg, Object... args) {
        out_.printf(msg, args);
    }

    public abstract void run() throws MalformedURLException;

    protected abstract Options getOptions();

    protected abstract String getDefaultAppname();

    protected WebDriver createDriver(CommandLine cmd) throws IOException, ParseException {
        if (cmd.hasOption("ses")) {
            URL seleniumUrl = new URL(cmd.getOptionValue("ses"));
            DesiredCapabilities caps = null;
            if (cmd.hasOption("cf")) {
                caps = readCapabilities(cmd.getOptionValue("cf"));
            } else { //browser or default
                caps = prepareCaps(parseBrowser(cmd));
            }
            return prepBrowser(seleniumUrl, caps);
        } else {
            return prepBrowser(parseBrowser(cmd));
        }
    }

    protected void validate(CommandLine cmd) throws ParseException {
        if (cmd.hasOption("cf") && cmd.hasOption("b"))
            throw new ParseException("cf and b parameters cannot be set together");
        if (cmd.hasOption("cf") && cmd.hasOption("b"))
            throw new ParseException("cf and b parameters cannot be set together");
        if (cmd.hasOption("cf") && !cmd.hasOption("ses"))
            throw new ParseException("'ses' option should be set when using 'cf'");
    }

    //k,bn,ba,an,as,cut,ml,w,vs,p,fp,cs,hs,cf,ses,b
    protected static Options BuildSharedOptions() {
        Options options = new Options();
        //Applitools flags
        options.addOption(Option.builder("k")
                .longOpt("apiKey")
                //.argName("key")
                .desc("Applitools api key")
                .required()
                .hasArg()
                .build());

        options.addOption(Option.builder("bn")
                .longOpt("BaselineName")
                .argName("name")
                .desc("Overrides baseline name for cross platform tests")
                .hasArg()
                .build()
        );

        options.addOption(Option.builder("ba")
                .longOpt("batchName")
                .argName("name")
                .desc("Set batch name to aggregate tests together")
                .hasArg()
                .build()
        );

        options.addOption(Option.builder("an")
                .longOpt("AppName")
                .argName("name")
                .desc("Set own application name, default: WebTester")
                .hasArg()
                .build()
        );

        options.addOption(Option.builder("as")
                .longOpt("ApplitoolsServer")
                .argName("url")
                .desc("Set Applitools server other than the custom")
                .hasArg()
                .build()
        );

        options.addOption(Option.builder("cut")
                .desc("Specify cut-out border around the original screenshot, header:footer:left:right, ie: 50:0:0:0")
                .argName("header:footer:left:right")
                .hasArg()
                .build());

        options.addOption(Option.builder("ml")
                .longOpt("MatchLevel")
                .desc(String.format("Set match level to one of [%s], default = Strict", Utils.getEnumValues(MatchLevels.class)))
                .hasArg()
                .argName("level")
                .build());

        options.addOption(Option.builder("w")
                .longOpt("wait")
                .desc("Set wait before screenshot, if scroll activated will take place between scrolling and taking the screenshot")
                .hasArg()
                .argName("seconds")
                .build());

        options.addOption(Option.builder("vs")
                .longOpt("viewportsize")
                .desc("Set consistent viewport-size of the browser, width:height, ie:1000x600")
                .hasArg()
                .argName("size")
                .build());

        options.addOption(Option.builder("p")
                .longOpt("proxy")
                .desc("Set proxy for Applitools")
                .hasArg()
                .argName("host")
                .build());

        options.addOption("fp", "fullPage", false, "Set full-page screenshot on all browsers");
        options.addOption("cs", "cssScroll", false, "Use css scrolling method instead of the original");
        options.addOption("hs", "hideScrollbars", false, "Set to hide scrollbars before screenshot");

        //Selenium flags
        options.addOption(Option.builder("cf")
                .longOpt("capsFile")
                .desc("Specify capabilities json file")
                .argName("filename")
                .hasArg()
                .build());

        options.addOption(Option.builder("ses")
                .longOpt("seleniumServer")
                .desc("Specify selenium server url")
                .argName("url")
                .hasArg()
                .build());

        String browsers = Utils.getEnumValues(Browser.class);
        options.addOption(Option.builder("b")
                .longOpt("com.applitools.Browser")
                .desc(String.format("com.applitools.Browser type [%s] default=Firefox", browsers))
                .hasArg()
                .type(Browser.class)
                .build());

        return options;
    }

    private static DesiredCapabilities prepareCaps(Browser browser) {
        switch (browser) {
            case Chrome:
                return DesiredCapabilities.chrome();
            case IE:
                return DesiredCapabilities.internetExplorer();
            case Safari:
                return DesiredCapabilities.safari();
            case Firefox:
                return DesiredCapabilities.firefox();
        }
        return new DesiredCapabilities();
    }

    private static Browser parseBrowser(CommandLine cmd) throws ParseException {
        try {
            return Browser.valueOf(StringUtils.capitalize(cmd.getOptionValue("b", "Firefox")));
        } catch (IllegalArgumentException e) {
            throw new ParseException("Wrong browser argument");
        }
    }

    private static WebDriver prepBrowser(Browser browser) {
        switch (browser) {
            case Chrome:
                return new ChromeDriver();
            case IE:
                return new InternetExplorerDriver();
            case Safari:
                return new SafariDriver();
            case Firefox:
                return new FirefoxDriver();
            default:
                return null;
        }
    }

    private static WebDriver prepBrowser(URL server, DesiredCapabilities caps) {
        return new RemoteWebDriver(server, caps);
    }

    private static DesiredCapabilities readCapabilities(String capsfile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return new DesiredCapabilities(
                (Map<String, ?>) mapper.readValue(
                        new File(capsfile),
                        new TypeReference<Map<String, Object>>() {
                        }));
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
}
