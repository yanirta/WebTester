import com.applitools.Modes.Session;
import com.applitools.Modes.SessionFactory;

import org.apache.commons.cli.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;


public class WebTester {
    public static void main(String[] args) {
        PrintStream out = System.out;
        SessionFactory factory = new SessionFactory(out, args);

        if (args.length == 0) {
            factory.PrintHelp();
            System.exit(-1);
        }

        try {
            Session session = factory.Build();
            session.run();
        } catch (ParseException e) {
            out.println(e.getMessage());
            factory.PrintHelp();
            System.exit(-1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            factory.PrintHelp();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            factory.PrintHelp();
            System.exit(-1);
        }
    }
}
