package com.SchoolioApi.source;

import com.SchoolioApi.DataSource;
import com.SchoolioApi.controllers.LessonSchedule;
import com.SchoolioApi.helpers.JsonConverter;

import java.sql.*;
import java.util.HashMap;

public class LessonScheduleSource {
    private final String sub;
    private final Connection con = DataSource.getConnection();
    private final JsonConverter jsonConverter = new JsonConverter();

    private static final String LESSON_SCHEDULE_TEMPLATE =
    """
        {
            "id": %s,
            "assigned_by": "%s",
            "lesson": {
                "id": %s,
                "name": "%s",
                "year": {
                    "id": %s,
                    "year": %s
                }
            },
            "day_of_week": "%s",
            "start": "%s",
            "end": "%s",
            "created_at": "%s",
            "updated_at": "%s"
        }
    """;

    public LessonScheduleSource(String sub) throws SQLException {
        this.sub = sub;
    }

    public String index() throws SQLException {
        String qry = """
                    SELECT
                        ls.__pk,
                        ls.assigned_by,
                        l.__pk,
                        l.name,
                        y.__pk,
                        y.year,
                        ls.day_of_week,
                        ls.start,
                        ls.end,
                        ls.created_at,
                        ls.updated_at
                    FROM lesson_schedule as ls
                    INNER JOIN lesson as l ON ls._fk_lesson = l.__pk
                    INNER JOIN year as y ON l._fk_year = y.__pk
                    WHERE sub = ?
                    ORDER BY ls.start
                """;

        PreparedStatement stmt = con.prepareStatement(qry);
        stmt.setString(1, sub);
        ResultSet rs = stmt.executeQuery();

        return jsonConverter.toJson(
                rs,
                LESSON_SCHEDULE_TEMPLATE
        );
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
