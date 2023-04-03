package com.SchoolioApi;

import com.SchoolioApi.helpers.ConfigFile;

import java.sql.*;
import java.util.Properties;

public class Database {
    private final Properties CONFIG_FILE = ConfigFile.config();
    public final Connection con;

    public Database() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://" + CONFIG_FILE.getProperty("DB_HOST") + "/" + CONFIG_FILE.getProperty("DB_NAME"),
                CONFIG_FILE.getProperty("DB_USER"),
                CONFIG_FILE.getProperty("DB_PASS")
        );
    }

    public String query(String query, Object[] params) throws Exception {
        Statement stmt = con.createStatement();
        query = String.format(query, params);
        ResultSet resultSet = stmt.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder jsonResult = new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = resultSet.getString(i);
                jsonResult.append(columnValue);
            }
        }
        con.close();
        return jsonResult.toString();
    }
}
