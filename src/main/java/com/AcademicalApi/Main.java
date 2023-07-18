package com.AcademicalApi;

import com.AcademicalApi.auth.AuthenticationFilter;
import com.AcademicalApi.controllers.*;
import com.AcademicalApi.helpers.ConfigFile;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import static spark.Spark.*;

public class Main {
    private static final Logger LOGGER_ALL_LEVELS = LogManager.getRootLogger();
    private static final Logger LOGGER_DEBUG = LogManager.getLogger("DebugLogger");
    private static final Logger LOGGER = LogManager.getLogger("ConsoleLogger");

    private static final String DEBUG_MODE = ConfigFile.config().getProperty("CONSOLE_DEBUG");
    static {
        if (!DEBUG_MODE.equals("on")) {
            Configurator.setLevel("ConsoleLogger", Level.OFF);
        }
    }

    public static void main(String[] args) {
        before((request, response) -> {
            response.type("application/json");

            String acceptHeader = request.headers("Accept");
            if (request.requestMethod().equals("GET") && (acceptHeader == null || !(acceptHeader.equals("*/*") || acceptHeader.equals("application/json")))) {
                response.status(HttpStatus.SC_NOT_ACCEPTABLE);
                halt("Must accept application/json");
            }
        });
        before("/secured/*", new AuthenticationFilter());

        path("/secured", () -> {
            post("/authenticate", (req, res) -> "true");
            get("/lesson-schedule", LessonSchedule::index);
            post("/lesson-schedule", LessonSchedule::store);
        });
        get("/year", Year::index);
        get("/lesson", Lesson::index);
        post("/get-bearer-by-creds", new BearerByCreds());
        post("/get-bearer-by-refresh", new BearerByRefresh());
        post("/register", new Register());
        get("/school-year-dates", SchoolYearDates::index);
        get("/week-option", WeekOption::index);
    }

    public static void logAll(Level level, Exception message) {
        LOGGER_ALL_LEVELS.log(level, message);
        LOGGER_DEBUG.log(level, message);
        LOGGER.log(level, message);
    }

    public static void logAll(Level level, String message) {
        LOGGER_ALL_LEVELS.log(level, message);
        LOGGER_DEBUG.log(level, message);
        LOGGER.log(level, message);
    }
}