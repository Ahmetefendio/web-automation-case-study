package com.nesine.poc.hooks;

import com.nesine.poc.context.TestContext;
import com.nesine.poc.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void startBrowser() {
        context.setDriver(DriverFactory.createChrome());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = context.getDriver();
        if (driver == null) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(png, "image/png", "Failure screenshot");
            }
        } catch (RuntimeException e) {
            LOG.warn("Could not take screenshot: {}", e.getMessage());
        } finally {
            driver.quit();
            LOG.info("{} -> {}", scenario.getName(), scenario.getStatus());
        }
    }
}
