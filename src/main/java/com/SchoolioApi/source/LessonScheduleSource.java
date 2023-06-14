package com.SchoolioApi.source;

import com.SchoolioApi.DataSource;
import com.SchoolioApi.helpers.JsonConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.SchoolioApi.controllers.LessonSchedule;

import java.sql.*;
import java.util.HashMap;

public class LessonScheduleSource {
    private final String sub;
    private final Connection con = DataSource.getConnection();

    private static final JsonConverter JSON_CONVERTER = new JsonConverter();

    public LessonScheduleSource(String sub) throws SQLException {
        this.sub = sub;
    }

    public String index() throws SQLException {
        String qry = """
                    SELECT JSON_ARRAY(
                        JSON_OBJECT(
                            'id', ls.__pk,
                            'lesson', JSON_OBJECT(
                                'id', l.__pk,
                                'name', l.name,
                                'year', JSON_OBJECT(
                                    'id', y.__pk,
                                    'year', y.year
                                )
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
                    WHERE sub = ?
                    ORDER BY ls.start
                """;

        PreparedStatement stmt = con.prepareStatement(qry);
        stmt.setString(1, sub);
        return JSON_CONVERTER.toJson(stmt.executeQuery());
    }

    public void store(String sub, HashMap<String, String> params) throws SQLException {
        String qry = """
            INSERT INTO lesson_schedule (
                sub,
                assigned_by,
                _fk_lesson,
                day_of_week,
                start,
                end
            ) VALUES (
                ?,
                ?,
                ?,
                ?,
                ?,
                ?
            )
        """;

        String assignedBy = params.get(LessonSchedule.queryParams[0]);
        int fkLesson = Integer.parseInt(params.get(LessonSchedule.queryParams[1]));
        String dayOfWeek = params.get(LessonSchedule.queryParams[2]);
        Time start = Time.valueOf(params.get(LessonSchedule.queryParams[3]));
        Time end = Time.valueOf(params.get(LessonSchedule.queryParams[4]));

        PreparedStatement stmt = con.prepareStatement(qry);
        stmt.setString(1, sub);
        stmt.setString(2, assignedBy);
        stmt.setInt(3, fkLesson);
        stmt.setString(4, dayOfWeek);
        stmt.setTime(5, start);
        stmt.setTime(6, end);

        stmt.execute();
    }
}
