package com.SchoolioApi;

import com.SchoolioApi.helpers.ConfigFile;
import org.apache.logging.log4j.Level;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {

    private static final Properties CONFIG_FILE = ConfigFile.config();
    private static final Connection connection;

    static {
        String url = "jdbc:mysql://" + CONFIG_FILE.getProperty("DB_HOST") + "/" + CONFIG_FILE.getProperty("DB_NAME");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, CONFIG_FILE.getProperty("DB_USER"), CONFIG_FILE.getProperty("DB_PASS"));
        } catch (ClassNotFoundException | SQLException e) {
            Main.logAll(Level.FATAL, "Failed to connect to database at DataSource: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
