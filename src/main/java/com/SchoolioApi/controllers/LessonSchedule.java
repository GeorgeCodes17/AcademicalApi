package com.SchoolioApi.controllers;

import com.SchoolioApi.Database;
import spark.Request;
import spark.Response;

public class LessonSchedule {
    private static final Database database;

    static {
        try {
            database = new Database();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String index(Request request, Response response) {
        try {
            if (request.params("sub").length() != 20) {
                response.status(422);
                return "Sub ID invalid";
            }
            String[] subId = {request.params("sub")};
            return database.query(
                """
                    SELECT JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', ls.__pk,
                            'lesson', JSON_OBJECT(
                                'id', l.__pk,
                                'name', l.name
                            ),
                            'year', JSON_OBJECT(
                                'id', y.__pk,
                                'name', y.year
                            ),
                            'day_of_week', ls.day_of_week,
                            'start', ls.start,
                            'end', ls.end,
                            'assigned_by', ls.assigned_by,
                            'created_at', ls.created_at,
                            'updated_at', ls.updated_at
                        )
                    ) FROM lesson_schedule as ls
                    INNER JOIN lesson as l ON ls._fk_lesson = l.__pk
                    INNER JOIN year as y ON l._fk_year = y.__pk
                    WHERE teacher = '%s'
                """,
                subId
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
