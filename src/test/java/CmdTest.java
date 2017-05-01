/**
 * Created by yanir on 24/04/2017.
 */
public abstract class CmdTest {
    public void execute(String cmdline) {
        WebTester.main(cmdline.split(" "));
    }
}
