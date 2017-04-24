package com.applitools.Modes.AttachedSession;

import org.openqa.selenium.remote.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class AttachedWebDriver extends RemoteWebDriver {

    public AttachedWebDriver(URL url, String sessionId) {
        super();
        setSessionId(sessionId);
        setCommandExecutor(new HttpCommandExecutor(url) {
            @Override
            public Response execute(Command command) throws IOException {
                if (command.getName() != "newSession") {
                    return super.execute(command);
                }
                try {
                    Field commandCodec = HttpCommandExecutor.class.getDeclaredField("commandCodec");
                    commandCodec.setAccessible(true);
                    commandCodec.set(this, Dialect.OSS.getCommandCodec());
                    //commandCodec.set(this, Dialect.W3C.getCommandCodec());
                    Field responseCodec = HttpCommandExecutor.class.getDeclaredField("responseCodec");
                    responseCodec.setAccessible(true);
                    responseCodec.set(this, Dialect.OSS.getResponseCodec());
                    //responseCodec.set(this, Dialect.W3C.getResponseCodec());

//                    Iterator var5 = this.additionalCommands.entrySet().iterator();
//
//                    while(var5.hasNext()) {
//                        Map.Entry entry = (Map.Entry)var5.next();
//                        this.defineCommand((String)entry.getKey(), (CommandInfo)entry.getValue());
//                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }


                return super.execute(new Command(getSessionId(), "getCapabilities"));
            }
        });
        startSession(new DesiredCapabilities());
    }
}
