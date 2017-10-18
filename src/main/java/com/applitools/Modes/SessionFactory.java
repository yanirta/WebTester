package com.applitools.Modes;

public class SessionFactory {
//    private PrintStream out_;
//    private static Options generalOptions_ = buildGeneralOptions();
//
//    public SessionFactory(PrintStream out) {
//        this.out_ = out;
//    }
//
//    public Session Build(String[] args) {
//        Commands mode_;
//        CommandLine cmd;
//
//        if (args.length == 0) {
//            PrintGeneralHelp();
//            System.exitWithCode(-1);
//            return null;
//        }
//
//        try {
//            cmd = Utils.parse(generalOptions_, args);
//            mode_ = Utils.parseEnum(Commands.class, cmd.getOptionValue("m"));
//        } catch (ParseException e) {
//            //e.printStackTrace();
//            PrintGeneralHelp();
//            System.exitWithCode(-1);
//            return null;
//        }
//
//        Session session = null;
//        //noinspection Since15
//        String[] modeArgs = Arrays.copyOfRange(args, 2, args.length);
//        switch (mode_) {
//            case Interactive:
//                session = new InteractiveTest(out_, modeArgs);
//                break;
//            case Iterate:
//                session = new IterativeTest(out_, modeArgs);
//                break;
//            case Single:
//                session = new SinglePageTest(out_, modeArgs);
//        }
//        return session;
//    }
//
//    public void PrintGeneralHelp() {
//        HelpFormatter formatter = new HelpFormatter();
//        formatter.setWidth(200);
//        formatter.printHelp("WebTester -m <ExecutionMode> -k <apiKey> <<mode specific args>> [options] \n", generalOptions_);
//    }
//
//    private static Options buildGeneralOptions() {
//        Options options = new Options();
//        options.addOption(Option.builder("m")
//                .longOpt("mode")
//                .desc(String.format("Execution mode [%s]", Utils.getEnumValues(Commands.class)))
//                .hasArg()
//                .argName("mode")
//                .required()
//                .build());
//
//        return options;
//    }
}
