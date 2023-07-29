package com.AcademicalApi.source;

import com.AcademicalApi.DataSource;
import com.AcademicalApi.controllers.LessonSchedule;
import com.AcademicalApi.helpers.JsonConverter;

import java.sql.*;
import java.text.ParseException;
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
                    "name": %s
                }
            },
            "week_option": "%s",
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
                        y.name,
                        ls.week_option,
                        ls.day_of_week,
                        ls.start,
                        ls.end,
                        ls.created_at,
                        ls.updated_at
                    FROM lesson_schedule as ls
                    INNER JOIN lesson as l ON ls._fk_lesson = l.__pk
                    INNER JOIN year as y ON l._fk_year = y.__pk
                    JOIN lesson_schedule_year_link as ll on ls.__pk = ll._fk_lesson_schedule
                    JOIN school_year_dates as sy on sy.__pk = ll._fk_school_year_dates
                    WHERE
                    sub = ? AND
                    CURDATE() > sy.first_day AND
                    CURDATE() < sy.last_day
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

    public void store(String sub, HashMap<String, String> params) throws SQLException, ParseException {
        String qry = """
            INSERT INTO lesson_schedule (
                sub,
                assigned_by,
                _fk_lesson,
                week_option,
                day_of_week,
                start,
                end
            ) VALUES (
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?
            )
        """;

        String assignedBy = params.get(LessonSchedule.REQUIRED_QUERY_PARAMS[0]);
        int fkLesson = Integer.parseInt(params.get(LessonSchedule.REQUIRED_QUERY_PARAMS[1]));
        String weekOption = params.get(LessonSchedule.REQUIRED_QUERY_PARAMS[2]);
        String dayOfWeek = params.get(LessonSchedule.REQUIRED_QUERY_PARAMS[3]);
        Time start = Time.valueOf(params.get(LessonSchedule.REQUIRED_QUERY_PARAMS[4]));
        Time end = Time.valueOf(params.get(LessonSchedule.REQUIRED_QUERY_PARAMS[5]));

        PreparedStatement stmt = con.prepareStatement(qry);
        stmt.setString(1, sub);
        stmt.setString(2, assignedBy);
        stmt.setInt(3, fkLesson);
        stmt.setString(4, weekOption);
        stmt.setString(5, dayOfWeek);
        stmt.setTime(6, start);
        stmt.setTime(7, end);
        stmt.execute();
    }
}
