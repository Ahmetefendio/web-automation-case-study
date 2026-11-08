package com.nesine.poc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// config/<env>.properties, every key can be overridden with -Dkey=value
public final class ConfigReader {

    private static final String ENV = System.getProperty("env", "prod");
    private static final Properties PROPS = new Properties();

    static {
        load("config/" + ENV + ".properties");
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        String value = PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing key '" + key + "' for env '" + ENV + "'");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static void load(String resource) {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Not found on classpath: " + resource);
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resource, e);
        }
    }
}
