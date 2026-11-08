package com.nesine.poc.driver;

import com.nesine.poc.config.ConfigReader;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createChrome() {
        ChromeOptions options = new ChromeOptions();
        // don't wait for ads and trackers, explicit waits cover what we use
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--window-size=1920,1080");
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new");
        }

        String gridUrl = ConfigReader.get("grid.url");
        if (gridUrl.isBlank()) {
            return new ChromeDriver(options);
        }
        try {
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url: " + gridUrl, e);
        }
    }
}
