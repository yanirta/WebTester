import com.applitools.Commands.*;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class WebTester {

    public static void main(String[] args) {
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
        test.exitWithCode();
    }
}
