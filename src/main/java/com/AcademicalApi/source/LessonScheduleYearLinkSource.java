package com.AcademicalApi.source;

import com.AcademicalApi.DataSource;
import com.AcademicalApi.Main;
import org.apache.logging.log4j.Level;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LessonScheduleYearLinkSource {
    private final Connection con = DataSource.getConnection();

    public boolean store(Date schoolYear) {
        String qry = """
            INSERT INTO lesson_schedule_year_link (
                _fk_lesson_schedule,
                _fk_school_year_dates
            ) VALUES (
                LAST_INSERT_ID(),
                (
                      SELECT __pk
                      FROM school_year_dates as sy
                      WHERE
                      ? > sy.first_day AND
                      ? < sy.last_day
                      LIMIT 1
                )
            )
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(qry);
            stmt.setDate(1, schoolYear);
            stmt.setDate(2, schoolYear);
            stmt.execute();
        } catch (SQLException e) {
            Main.logAll(Level.TRACE, e);
            return false;
        }
        return true;
    }

    public void delete() throws SQLException {
        con.prepareStatement("DELETE FROM lesson_schedule WHERE __pk = LAST_INSERT_ID()").execute();
    }
}
