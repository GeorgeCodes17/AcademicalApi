package com.SchoolioApi.controllers;

import com.SchoolioApi.helpers.JsonConverter;
import com.SchoolioApi.source.LessonScheduleSource;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class LessonSchedule {
    private static final JsonConverter jsonConverter = new JsonConverter();

    public static String index(Request request, Response response) {
        try {
            LessonScheduleSource lessonScheduleSource = new LessonScheduleSource(request.params("sub"));
            return jsonConverter.toJson(lessonScheduleSource.index());
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        }
    }
}
