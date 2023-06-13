package com.SchoolioApi.controllers;

import com.SchoolioApi.Main;
import com.SchoolioApi.exceptions.LessonException;
import com.SchoolioApi.helpers.JsonConverter;
import com.SchoolioApi.source.LessonSource;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class Lesson {
    private static final JsonConverter jsonConverter = new JsonConverter();

    public static String index(Request request, Response response) {
        try {
            LessonSource lessonSource = new LessonSource();
            return jsonConverter.toJson(lessonSource.index());
        } catch (SQLException e) {
            Main.logAll(Level.WARN, new LessonException("Failed to get lessons at Lesson.index: " + e.getMessage()));
            response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return e.getMessage();
        }
    }
}
