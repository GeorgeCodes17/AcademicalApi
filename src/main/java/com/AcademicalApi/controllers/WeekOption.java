package com.AcademicalApi.controllers;

import com.AcademicalApi.Main;
import com.AcademicalApi.source.WeekOptionSource;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class WeekOption {
    public static String index(Request request, Response response) {
        try {
            return new WeekOptionSource().index();
        } catch (SQLException e) {
            Main.logAll(Level.WARN, e);
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }
}
