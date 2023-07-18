package com.AcademicalApi.objects;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SchoolYearDatesObject {
    @SerializedName("id") private final int id;
    @SerializedName("year") private final int year;
    @SerializedName("first_day") private final Date firstDay;
    @SerializedName("last_day") private final Date lastDay;

    public SchoolYearDatesObject(int id, int year, Date firstDay, Date lastDay) {
        this.id = id;
        this.year = year;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public Date getFirstDay() {
        return firstDay;
    }

    public Date getLastDay() {
        return lastDay;
    }
}

