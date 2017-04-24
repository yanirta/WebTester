import com.applitools.Commands.*;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebTester {
    public static String CURR_VER = "0.4"; //TODO find more suitable place and logic

    public static void main(String[] args) {
//        Logger.getLogger("geckodriver").setLevel(Level.OFF);
//        Logger.getLogger("mozprofile::profile").setLevel(Level.OFF);
//        Logger.getLogger("geckodriver::marionette").setLevel(Level.OFF);
//        Logger.getLogger("Marionette").setLevel(Level.OFF);
//        Logger.getLogger("org.openqa.selenium.remote.ProtocolHandshake").setLevel(Level.OFF);

        JCommander jc = new JCommander();
        jc.setProgramName("WebTester");
        jc.setColumnSize(200);

        SinglePageTest singlePageTest = new SinglePageTest();
        IterativeTest iterativeTest = new IterativeTest();
        InteractiveTest interactiveTest = new InteractiveTest();

        jc.addCommand(Commands.Single.toString(), singlePageTest);
        jc.addCommand(Commands.Iterate.toString(), iterativeTest);
        jc.addCommand(Commands.Interactive.toString(), interactiveTest);

        try {
            jc.parse(args);
        } catch (ParameterException e) {
            System.out.printf("Parameter(s) error: %s \n", e.getMessage());
            System.exit(0);
        }

        if (jc.getParsedCommand() == null) {
            jc.usage();
            System.exit(0);
        }

        ITest test = (ITest) jc.getCommands().get(jc.getParsedCommand()).getObjects().get(0);
        test.Run();
    }
}
