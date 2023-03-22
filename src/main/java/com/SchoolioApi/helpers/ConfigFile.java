package com.SchoolioApi.helpers;

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
            throw new RuntimeException(e);
        }
    }
}
