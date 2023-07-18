package com.AcademicalApi.helpers;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String dateString = jsonElement.getAsString();
        try {
            return new Date(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new JsonParseException("Error parsing date: " + dateString, e);
        }
    }
}
