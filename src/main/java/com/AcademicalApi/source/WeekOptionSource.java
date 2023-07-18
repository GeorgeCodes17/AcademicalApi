package com.AcademicalApi.source;

import com.AcademicalApi.helpers.DateDeserializer;
import com.AcademicalApi.objects.SchoolYearDatesObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class WeekOptionSource {
    public String index() throws SQLException {
        String schoolYear = new SchoolYearDatesSource().index();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
        SchoolYearDatesObject schoolYearDatesObject = gson.fromJson(schoolYear, SchoolYearDatesObject.class);

        LocalDate yearFirstDay = LocalDate.ofInstant(Instant.ofEpochMilli(
                schoolYearDatesObject.getFirstDay().getTime()
        ), ZoneId.systemDefault());
        long weeksElapsed = ChronoUnit.WEEKS.between(yearFirstDay, LocalDate.now());

        boolean weeksElapsedIsEven = weeksElapsed % 2 == 0;
        return weeksElapsedIsEven ? "a" : "b";
    }
}
