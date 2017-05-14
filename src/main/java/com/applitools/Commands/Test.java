package com.applitools.Commands;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public abstract class Test implements ITest {
    private static final String BUGIFY_FILENAME = "bugify.js";

    private String bugify_js_ = null;

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

    public void Init() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        init_bugify();
    }

    public abstract void Execute();

    public abstract void TearDown();

    public void bugify(WebDriver driver) {
        if (null != bugify_js_) {
            try {
                Thread.sleep(0);
                ((JavascriptExecutor) driver).executeScript(bugify_js_);
            } catch (InterruptedException e) {
                System.out.printf("Interupted exception in sleep \n");
                System.out.printf("%s \n", e.getMessage());
                System.out.printf("Cause %s \n", e.getCause().getMessage());
            }
        }
    }

    private void init_bugify() throws IOException {
        File bugifyfile = new File(BUGIFY_FILENAME);
        if (bugifyfile.exists()) {
            System.out.printf("Using %s \n", BUGIFY_FILENAME);
            FileInputStream inputStream = new FileInputStream(bugifyfile);
            try {
                bugify_js_ = IOUtils.toString(inputStream);
            } finally {
                inputStream.close();
            }
        }
    }
}
