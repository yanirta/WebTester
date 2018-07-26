package com.applitools.Commands;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public abstract class Test implements ITest {
    public static final String CURR_VERSION = "0.1.9";

    private static final String JS_EXECUTE_BEFORE_STEP_FILENAME = "execute_before_step.js";
    private static final String JS_EXECUTE_AFTER_STEP_FILENAME = "execute_after_step.js"; //TODO

    private String execute_before_step_js_ = null;
    private int exitcode = 0;

    public final void Run() {
        try {
            ValidateParams();
            Init();
            Execute();
            TearDown();
        } catch (MalformedURLException e) {
            printException(e);
        } catch (IOException e) {
            printException(e);
        } catch (URISyntaxException e) {
            printException(e);
        } catch (ParserConfigurationException e) {
            printException(e);
        } catch (SAXException e) {
            printException(e);
        } finally {
            TearDown();
        }
    }

    public abstract void ValidateParams();

    public void Init() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        initJsExecutes();
    }

    public abstract void Execute();

    public abstract void TearDown();

    public void executeJsBeforeStep(WebDriver driver) {
        if (null != execute_before_step_js_) {
            try {
                (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                //Thread.sleep(1000); //TODO
                ((JavascriptExecutor) driver).executeScript(execute_before_step_js_);
                //Thread.sleep(1000); //TODO
//            } catch (InterruptedException e) {
//                System.out.printf("Interrupted exception in sleep \n");
//                System.out.printf("%s \n", e.getMessage());
//                System.out.printf("Cause %s \n", e.getCause().getMessage());
            } catch (JavascriptException e) {
                System.out.printf("Error in JS execution: %s", e.getMessage());
            }
        }

    }

    private void initJsExecutes() throws IOException {
        File executeJS = new File(JS_EXECUTE_BEFORE_STEP_FILENAME);
        if (executeJS.exists()) {
            System.out.printf("Using %s \n", JS_EXECUTE_BEFORE_STEP_FILENAME);
            FileInputStream inputStream = new FileInputStream(executeJS);
            try {
                execute_before_step_js_ = IOUtils.toString(inputStream);
            } finally {
                inputStream.close();
            }
        }
    }

    private void printException(Exception e) {
        System.out.printf("Error: %s", e.getMessage());
        e.printStackTrace();
    }

    public void increaseExitCode() {
        exitcode++;
    }

    public void exitWithCode() {
        System.exit(exitcode);
    }
}
