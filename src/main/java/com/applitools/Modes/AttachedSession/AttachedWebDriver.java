package com.applitools.Modes.AttachedSession;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.*;

import java.io.IOException;
import java.net.URL;

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
                return super.execute(new Command(getSessionId(), "getCapabilities"));
            }
        });
        startSession(new DesiredCapabilities());
    }
}
