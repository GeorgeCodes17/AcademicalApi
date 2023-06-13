package com.SchoolioApi.controllers;

import com.SchoolioApi.Main;
import com.SchoolioApi.exceptions.LessonScheduleException;
import com.SchoolioApi.helpers.JsonConverter;
import com.SchoolioApi.source.LessonScheduleSource;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.HashMap;

public class LessonSchedule {
    private static final JsonConverter jsonConverter = new JsonConverter();
    public static final String[] queryParams = {"assigned_by", "lesson", "day_of_week", "start", "end"};

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

    public static String store(Request request, Response response) {
        if (request.headers("SubId") == null || request.headers("SubId").isEmpty()) {
            response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
            return "SubId is a required parameter in the request header";
        }

        HashMap<String, String> params = new HashMap<>();
        for (String param: queryParams) {
            if (request.queryParams(param) == null) {
                response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
                return "Missing query parameter: " + param;
            }
            params.put(param, request.queryParams(param));
        }

        try {
            LessonScheduleSource lessonScheduleSource = new LessonScheduleSource(request.headers("SubId"));
            lessonScheduleSource.store(request.headers("SubId"), params);
        } catch (SQLException e) {
            Main.logAll(Level.WARN, new SQLException("Failed to store lesson schedule: " + e.getMessage()));
            response.status(HttpStatus.SC_BAD_REQUEST);
            return e.getMessage();
        }
        return "success";
    }
}
