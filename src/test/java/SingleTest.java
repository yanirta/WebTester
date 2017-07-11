import com.applitools.Commands.SeleniumTest;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SingleTest extends CmdTest {
    @Test
    public void Simple() {
        run("");
    }

    @Test
    public void SimpleFirefox() {
        simpleBrowserTest("Firefox");
    }

    @Test
    public void SimpleChrome() {
        simpleBrowserTest("Chrome");
    }

    @Test
    public void SimpleFullParamsTest() {
        simpleBrowserFullParamsTest("SingleTestApp", "Chrome", "Layout2", "1000x700", "SingleTestBatch");
    }

    @Test
    public void TestLocalCapabilitiesChrome() {
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", "Google Nexus 5");

        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation", mobileEmulation);

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        SeleniumTest.writeCapabilities(capabilities, new File("emulated_device.json"));

        execute(String.format("Single -k %s -br %s -ba %s -cf %s -pu %s -sr %s",
                System.getenv("API_KEY"),
                "Chrome",
                "LocalCaps",
                "emulated_device.json",
                "http://www.asos.com/",
                "1"
        ));
    }

    private void simpleBrowserFullParamsTest(String appname, String browser, String matchlevel, String viewportsize, String batchname) {
        execute(String.format("-an %s -br %s -ml %s -vs %s -ba %s", appname, browser, matchlevel, viewportsize, batchname));
    }

    private void simpleBrowserTest(String browser) {
        run(String.format("-br %s", browser));
    }

    private void run(String moreparams) {
        String website = "https://applitools.com";
        String cmd = String.format("Single -k %s -pu %s %s", System.getenv("API_KEY"), website, moreparams);
        execute(cmd);
    }
}