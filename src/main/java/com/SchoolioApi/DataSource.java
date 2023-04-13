package com.SchoolioApi;

import com.SchoolioApi.helpers.ConfigFile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {

    private static final Properties CONFIG_FILE = ConfigFile.config();
    private static final HikariConfig HIKARI_CONFIG = new HikariConfig();
    private static final HikariDataSource DATA_SOURCE;

    static {
        HIKARI_CONFIG.setJdbcUrl("jdbc:mysql://" + CONFIG_FILE.getProperty("DB_HOST") + "/" + CONFIG_FILE.getProperty("DB_NAME"));
        HIKARI_CONFIG.setUsername(CONFIG_FILE.getProperty("DB_USER"));
        HIKARI_CONFIG.setPassword(CONFIG_FILE.getProperty("DB_PASS") );
        HIKARI_CONFIG.addDataSourceProperty( "cachePrepStmts" , "true" );
        HIKARI_CONFIG.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        HIKARI_CONFIG.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        DATA_SOURCE = new HikariDataSource(HIKARI_CONFIG);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
