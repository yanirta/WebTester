package com.applitools.Modes;

import com.applitools.Modes.AttachedSession.AttachedTest;
import com.applitools.Modes.EnumeratedSession.EnumeratedTest;
import com.applitools.Modes.InteractiveSession.InteractiveTest;
import com.applitools.Utils;
import com.sun.istack.internal.Nullable;
import org.apache.commons.cli.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class SessionFactory {
    private PrintStream out_;
    private String[] args_;
    private static Options generalOptions_ = BuildOptions();

    public SessionFactory(PrintStream out, String[] args) {
        this.out_ = out;
        this.args_ = args;
    }

    public Session Build() throws ParseException, URISyntaxException, IOException {
        CommandLine cmd = Utils.parse(generalOptions_, args_);

        Mode mode_ = Utils.parseEnum(Mode.class, cmd.getOptionValue("m"));

        if (cmd.hasOption("h")) {
            PrintHelp(mode_);
            System.exit(0);
        }

        Session session = null;

        try {
            switch (mode_) {
                case Interactive:
                    //Building second set of options to contain general options as well
                    // otherwise the parser will not work
                    BuildOptions(InteractiveTest.options);
                    session = new InteractiveTest(out_, args_);
                    break;
                case Attach:
                    //Building second set of options to contain general options as well
                    // otherwise the parser will not work
                    BuildOptions(AttachedTest.options);
                    session = new AttachedTest(out_, args_);
                    break;
                case Enumerate:
                    //Building second set of options to contain general options as well
                    // otherwise the parser will not work
                    BuildOptions(EnumeratedTest.options);
                    session = new EnumeratedTest(out_, args_);
                    break;
            }
        } catch (ParseException e) {
            out_.println(e.getMessage());
            PrintHelp(mode_);
            System.exit(-1);
        } catch (ParserConfigurationException e) {
            out_.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        return session;
    }

    public void PrintHelp() {
        PrintHelp(null);
    }

    public void PrintHelp(@Nullable Mode mode) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
        if (mode == null) {
            formatter.printHelp("WebTester -m <ExecutionMode> -k <apiKey> <<mode specific args>> [options] \n", generalOptions_);
        } else {
            Options options = null;
            switch (mode) {
                case Interactive:
                    options = InteractiveTest.options;
                    formatter.printHelp("WebTester -m Interactive -k <apiKey> [options] \n", options);
                    break;
                case Attach:
                    options = AttachedTest.options;
                    formatter.printHelp("WebTester -m Attach -k <apiKey> -id <SessionId> [options] \n", options);
                    break;
                case Enumerate:
                    //TODO
                    options = EnumeratedTest.options;
                    formatter.printHelp("WebTester -m Enumerate -k <apiKey> -l <url/path> [options] \n", options);
                    break;
            }
        }
    }

    private static Options BuildOptions() {
        return BuildOptions(null);
    }

    private static Options BuildOptions(Options options) {
        options = options == null ? new Options() : options;
        options.addOption(Option.builder("m")
                .longOpt("mode")
                .desc(String.format("Execution mode [%s]", Utils.getEnumValues(Mode.class)))
                .hasArg()
                .argName("mode")
                .required()
                .build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("Get help on a specific mode")
                .build());

        return options;
    }
}
