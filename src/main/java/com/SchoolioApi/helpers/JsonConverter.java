package com.SchoolioApi.helpers;

import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class JsonConverter {

    private final Gson gson = new Gson();

    public HashMap toHashmap(String jsonString) {
        return gson.fromJson(jsonString, HashMap.class);
    }

    public String toJson(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        StringBuilder jsonResult = new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = resultSet.getString(i);
                jsonResult.append(columnValue);
            }
        }
        return jsonResult.toString();
    }
}
