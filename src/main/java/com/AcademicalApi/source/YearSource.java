package com.AcademicalApi.source;

import com.AcademicalApi.DataSource;

import java.sql.*;

public class YearSource {
    private final Connection con = DataSource.getConnection();

    public ResultSet index() throws SQLException {
        String qry = "SELECT JSON_ARRAYAGG(year) FROM year";
        Statement stmt = con.createStatement();
        return stmt.executeQuery(qry);
    }
}
