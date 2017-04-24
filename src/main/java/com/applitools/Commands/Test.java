package com.applitools.Commands;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public abstract class Test implements ITest {
    public final void Run() {
        try {
            ValidateParams();
            Init();
            Execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            TearDown();
        }
    }

    public abstract void ValidateParams();

    public abstract void Init() throws IOException, URISyntaxException, ParserConfigurationException, SAXException;

    public abstract void Execute();

    public abstract void TearDown();
}
