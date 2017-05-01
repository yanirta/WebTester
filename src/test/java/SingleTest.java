import org.junit.Test;

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

    private void simpleBrowserFullParamsTest(String appname, String browser, String matchlevel, String viewportsize, String batchname) {
        run(String.format("-an %s -br %s -ml %s -vs %s -ba %s", appname, browser, matchlevel, viewportsize, batchname));
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