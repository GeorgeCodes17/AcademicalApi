package com.SchoolioApi.controllers;

import com.SchoolioApi.source.LessonScheduleSource;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class LessonSchedule {
    public static String index(Request request, Response response) {
        if (request.headers("SubId") == null || request.headers("SubId").isEmpty()) {
            response.status(422);
            return "SubId is a required parameter in the request header";
        }
        try {
            return new LessonScheduleSource(request.headers("SubId")).index();
        } catch (SQLException e) {
            response.status(500);
            return e.getMessage();
        }
    }
}
