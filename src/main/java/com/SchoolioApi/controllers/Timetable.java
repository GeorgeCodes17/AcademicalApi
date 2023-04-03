package com.SchoolioApi.controllers;

import com.SchoolioApi.Database;
import spark.Request;
import spark.Response;

public class Timetable {
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
                            '__pk', __pk,
                            'teacher', teacher,
                            'assigned_by', assigned_by,
                            '_fk_lesson', _fk_lesson,
                            'day_of_week', day_of_week,
                            'start', start,
                            'end', end,
                            'created_at', created_at,
                            'updated_at', updated_at
                        )
                    ) FROM timetable WHERE teacher = '%s'
                """,
                subId
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
