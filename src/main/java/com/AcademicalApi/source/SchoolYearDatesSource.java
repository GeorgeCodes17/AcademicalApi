package com.AcademicalApi.source;

import com.AcademicalApi.DataSource;
import com.AcademicalApi.helpers.JsonConverter;

import java.sql.*;

public class SchoolYearDatesSource {
    private final Connection con = DataSource.getConnection();

    public String index() throws SQLException {
        String qry = """
            SELECT
            JSON_OBJECT(
                'id', __pk,
                'year', school_year,
                'first_day', first_day,
                'last_day', last_day
            )
            FROM school_year_dates
            WHERE CURDATE() > first_day AND CURDATE() < last_day
            LIMIT 1
        """;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(qry);
        return new JsonConverter().toJson(rs);
    }
}
