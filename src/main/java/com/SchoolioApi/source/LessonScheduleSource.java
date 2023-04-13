package com.SchoolioApi.source;

import com.SchoolioApi.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LessonScheduleSource {
    private final String sub;
    private final Connection con = DataSource.getConnection();

    public LessonScheduleSource(String sub) throws SQLException {
        this.sub = sub;
    }

    public ResultSet index() throws SQLException {
        String qry = """
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
            WHERE teacher = ?
        """;

        PreparedStatement stmt = con.prepareStatement(qry);
        stmt.setString(1, sub);
        return stmt.executeQuery();
    }
}