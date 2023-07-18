package com.AcademicalApi.controllers;

import com.AcademicalApi.Main;
import com.AcademicalApi.source.SchoolYearDatesSource;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class SchoolYearDates {
    private static final SchoolYearDatesSource SCHOOL_YEAR_DATES_SOURCE = new SchoolYearDatesSource();

    public static String index(Request request, Response response) {
        try {
            return SCHOOL_YEAR_DATES_SOURCE.index();
        } catch (SQLException e) {
            Main.logAll(Level.ERROR, e);
            response.status(HttpStatus.SC_NOT_FOUND);
            return "Couldn't find a school year record under current year";
        }
    }
}
