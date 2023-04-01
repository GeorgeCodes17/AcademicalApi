package com.SchoolioApi.auth;

import com.okta.jwt.JwtVerificationException;
import org.apache.http.HttpStatus;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.halt;

public class AuthenticationFilter implements Filter {
    private final List<String> IGNORE_ENDPOINTS = List.of(
            "/register",
            "/get-bearer-by-creds",
            "/get-bearer-by-refresh"
    );

    @Override
    public void handle(Request request, Response response) {
        String requestedEndpoint = request.pathInfo();
        if (IGNORE_ENDPOINTS.contains(requestedEndpoint)) {
            return;
        }

        String accessToken = request.headers("Authorization");
        try {
            ValidateAccessToken.isValid(accessToken);
        } catch (JwtVerificationException e) {
            halt(HttpStatus.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
