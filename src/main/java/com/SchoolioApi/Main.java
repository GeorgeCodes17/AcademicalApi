package com.SchoolioApi;

import com.SchoolioApi.auth.AuthenticationFilter;
import com.SchoolioApi.controllers.BearerByCreds;
import com.SchoolioApi.controllers.BearerByRefresh;
import com.SchoolioApi.controllers.Register;
import com.SchoolioApi.controllers.Timetable;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        before("/secured/*", new AuthenticationFilter());

        path("/secured", () -> {
            post("/authenticate", (req, res) -> "true");
            get("/timetable/get/:sub", Timetable::index);
        });
        post("/get-bearer-by-creds", new BearerByCreds());
        post("/get-bearer-by-refresh", new BearerByRefresh());
        post("/register", new Register());
    }
}