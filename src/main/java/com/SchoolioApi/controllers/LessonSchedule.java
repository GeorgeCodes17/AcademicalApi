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

public class LessonSchedule {
    private static final JsonConverter jsonConverter = new JsonConverter();

    public static String index(Request request, Response response) {
        if (request.headers("SubId") == null || request.headers("SubId").isEmpty()) {
            response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
            return "SubId is a required parameter in the request header";
        }
        try {
            LessonScheduleSource lessonScheduleSource = new LessonScheduleSource(request.headers("SubId"));
            return jsonConverter.toJson(lessonScheduleSource.index());
        } catch (SQLException e) {
            Main.logAll(Level.WARN, new LessonScheduleException("Failed to get lesson schedule by sub id: " + e.getMessage()));
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
    }
}
