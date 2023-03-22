package com.SchoolioApi;

import com.SchoolioApi.auth.AuthenticationFilter;
import com.SchoolioApi.controllers.Register;
import com.SchoolioApi.controllers.TokenFromCreds;
import com.SchoolioApi.controllers.TokenFromRefresh;
import com.okta.jwt.JwtVerificationException;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws JwtVerificationException {
        before(new AuthenticationFilter());

        post("/authenticate", (req, res) -> "true");
        post("/get-bearer-by-creds", new TokenFromCreds());
        post("/get-bearer-by-refresh", new TokenFromRefresh());
        post("/register", new Register());
    }
}