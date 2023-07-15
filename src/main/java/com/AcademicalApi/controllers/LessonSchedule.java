package com.AcademicalApi.controllers;

import com.AcademicalApi.Main;
import com.AcademicalApi.exceptions.LessonScheduleException;
import com.AcademicalApi.source.LessonScheduleSource;
import com.AcademicalApi.source.LessonScheduleYearLinkSource;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class LessonSchedule {
    public static final String[] REQUIRED_QUERY_PARAMS = {"assigned_by", "lesson", "week_option", "day_of_week", "start", "end"};
    private static final LessonScheduleYearLinkSource LESSON_SCHEDULE_YEAR_LINK_SOURCE = new LessonScheduleYearLinkSource();

    public static String index(Request request, Response response) {
        if (request.headers("SubId") == null || request.headers("SubId").isEmpty()) {
            response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
            return "SubId is a required parameter in the request header";
        }
        try {
            return new LessonScheduleSource(request.headers("SubId")).index();
        } catch (SQLException e) {
            Main.logAll(Level.WARN, new LessonScheduleException("Failed to get lesson schedule by sub id: " + e.getMessage()));
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
    }

    /**
     *
     * Parameters
     * assigned_by: Required String
     * lesson:      Required, Int
     * week_option: Required, ENUM
     * day_of_week: Required, ENUM
     * start:       Required, TIME
     * end:         Required, TIME
     * school_year: Optional, Int, example: "2023-12-31"
     */
    public static String store(Request request, Response response) {
        if (request.headers("SubId") == null || request.headers("SubId").isEmpty()) {
            response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
            return "SubId is a required parameter in the request header";
        }

        HashMap<String, String> params = new HashMap<>();
        for (String param: REQUIRED_QUERY_PARAMS) {
            if (request.queryParams(param) == null) {
                response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
                return "Missing query parameter: " + param;
            }
            params.put(param, request.queryParams(param));
        }
        // Optional param
        params.put("school_year", (request.queryParams("school_year") != null) ? request.queryParams("school_year") : LocalDate.now().toString());

        try {
            LessonScheduleSource lessonScheduleSource = new LessonScheduleSource(request.headers("SubId"));
            lessonScheduleSource.store(request.headers("SubId"), params);

            Date schoolYear = Date.valueOf(params.get("school_year"));
            boolean qryYearLinkSuccess = LESSON_SCHEDULE_YEAR_LINK_SOURCE.store(schoolYear);
            if (!qryYearLinkSuccess) {
                LESSON_SCHEDULE_YEAR_LINK_SOURCE.delete();
                response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
                return (request.queryParams("school_year") != null) ? "failed- likely cause the school_year entered is wrong" : "failed";
            }
            return "success";
        } catch (SQLException e) {
            Main.logAll(Level.WARN, new SQLException("Failed to store lesson schedule: " + e.getMessage()));
            response.status(HttpStatus.SC_BAD_REQUEST);
            return e.getMessage();
        } catch (java.text.ParseException e) {
            Main.logAll(Level.WARN, e);
            response.status(HttpStatus.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }
}
