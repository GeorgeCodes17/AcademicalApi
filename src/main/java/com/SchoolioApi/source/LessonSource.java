package com.SchoolioApi.source;

import com.SchoolioApi.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LessonSource {
    private final Connection con = DataSource.getConnection();

    public ResultSet index() throws SQLException {
        String qry = """
          SELECT JSON_ARRAYAGG(
              JSON_OBJECT(
                  'id', l.__pk,
                  'name', l.name,
                  'year', JSON_OBJECT(
                    'id', y.__pk,
                    'year', y.year
                  ),
                  'created_at', l.created_at,
                  'updated_at', l.updated_at,
                  'deleted_at', l.deleted_at
              )
          ) FROM lesson as l
          JOIN year as y on l._fk_year = y.__pk""";
        Statement stmt = con.createStatement();
        return stmt.executeQuery(qry);
    }
}
