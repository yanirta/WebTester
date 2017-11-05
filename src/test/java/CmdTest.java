/**
 * Created by yanir on 24/04/2017.
 */
public abstract class CmdTest {
    public void execute(String cmdline) {
        WebTester.main(cmdline.split(" "));
    }

    public void execute(String mode, String apiKey, String the_rest) {
        execute(String.format("%s -k %s %s", mode, apiKey, the_rest));
    }
}
