package com.SchoolioApi.controllers;

import com.SchoolioApi.Main;
import com.SchoolioApi.exceptions.YearException;
import com.SchoolioApi.helpers.JsonConverter;
import com.SchoolioApi.source.YearSource;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class Year {
    private static final JsonConverter jsonConverter = new JsonConverter();

    public static String index(Request request, Response response) {
        try {
            YearSource yearSource = new YearSource();
            return jsonConverter.toJson(yearSource.index());
        } catch (SQLException e) {
            Main.logAll(Level.WARN, new YearException("Failed to get years at Year.index: " + e.getMessage()));
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
    }
}
