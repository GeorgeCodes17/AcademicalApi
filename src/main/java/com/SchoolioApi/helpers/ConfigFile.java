package com.SchoolioApi.helpers;

import com.SchoolioApi.Main;
import org.apache.logging.log4j.Level;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigFile {
    public static Properties config() {
        try {
            FileInputStream configFile = new FileInputStream("config.properties");
            Properties getConfigFile = new Properties();
            getConfigFile.load(configFile);
            return getConfigFile;
        } catch (Exception e) {
            Main.logAll(Level.FATAL, "Failed to get config file: " + e);
            throw new RuntimeException(e);
        }
    }
}
