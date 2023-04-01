package com.SchoolioApi.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import spark.ResponseTransformer;

import java.util.HashMap;

public class JsonConverter implements ResponseTransformer {

    private final Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

    public HashMap render(String jsonString) {
        return gson.fromJson(jsonString, HashMap.class);
    }

    public boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
