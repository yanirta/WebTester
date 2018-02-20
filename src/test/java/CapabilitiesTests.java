import com.applitools.Commands.SeleniumTest;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanir on 10/07/2017.
 */
public class CapabilitiesTests {
    @Test
    public void pourToFile() {
        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "Nexus 5");


        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://applitools.com");
//
//        Map<String, String> mobileEmulation = new HashMap<String, String>();
//        mobileEmulation.put("deviceName", "Google Nexus 5");
//
//        Map<String, Object> chromeOptions = new HashMap<String, Object>();
//        chromeOptions.put("mobileEmulation", mobileEmulation);
//
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        SeleniumTest.writeCapabilities(chromeOptions, new File("chrome_emulation_Nexus5.json"));
    }

    @Test
    public void chromeOptionsToFile(){
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        //options.addArguments("headless");
        options.addArguments("debug-devtools");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        SeleniumTest.writeCapabilities(capabilities, new File("chrome_options.json"));
    }
}
